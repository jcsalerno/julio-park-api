package com.julio.park_api.web.controller;
import com.julio.park_api.entity.Cliente;
import com.julio.park_api.entity.ClienteVaga;
import com.julio.park_api.repository.projection.ClienteVagaProjection;
import com.julio.park_api.service.ClienteVagaService;
import com.julio.park_api.service.EstacionamentoService;
import com.julio.park_api.web.dto.EstacionamentoCreateDto;
import com.julio.park_api.web.dto.EstacionamentoResponseDto;
import com.julio.park_api.web.dto.PageableDto;
import com.julio.park_api.web.dto.mapper.ClienteVagaMapper;
import com.julio.park_api.web.dto.mapper.PageableMapper;
import com.julio.park_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@Tag(name = "Estacionamentos", description = "Operações de registro de entrada e saída de um veículo do estacionamento")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/estacionamentos")
public class EstacionamentoController {

    private final EstacionamentoService estacionamentoService;

    private final ClienteVagaService clienteVagaService;

    @Operation(summary = "Operação de check-in",
              description = "Recurso para dar entrada de um veículo no estacionamento" +
                      "Requisição exige uso de um Bearer Token. Acesso restrito a Role='ADMIN'",
              security = @SecurityRequirement(name = "security"),
    responses = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Recurso criado com sucesso",
                    headers = @Header(
                            name = HttpHeaders.LOCATION,
                            description = "URL de acesso ao recurso criado"
                    ),
                    content = @Content(
                            mediaType = "application/json;charset=UTF-8",
                            schema = @Schema(implementation = EstacionamentoResponseDto.class)
                    )
            ),

            @ApiResponse(responseCode = "404", description = "Causas possíveis: <br/>" +
                "- CPF do cliente não cadastrado no sistema; <br/>" +
                "- Nenhuma vaga livre foi localizada",
                content = @Content(mediaType = "application/json;charset=UTF-8",
                        schema = @Schema(implementation = ErrorMessage.class))),

        @ApiResponse(responseCode = "422",
                description = "Recurso não processado por falta de dados ou dados inválidos",
                content = @Content(mediaType = "application/json;charset=UTF-8",
                        schema = @Schema(implementation = ErrorMessage.class))),

        @ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil do cliente",
                content = @Content(mediaType = "application/json;charset=UTF-8",
                        schema = @Schema(implementation = ErrorMessage.class))),
    })

    @PostMapping("/check-in")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstacionamentoResponseDto> checkin(@RequestBody @Valid EstacionamentoCreateDto dto) {
        ClienteVaga clienteVaga = ClienteVagaMapper.toClienteVaga(dto);
        estacionamentoService.checkIn(clienteVaga);
        EstacionamentoResponseDto responseDto = ClienteVagaMapper.toDto(clienteVaga);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{recibo}")
                .buildAndExpand(clienteVaga.getRecibo())
                .toUri();

        return ResponseEntity.created(location).body(responseDto);
    }

    @Operation(
            summary = "Buscar estacionamento pelo recibo",
            description = "Retorna as informações do estacionamento associado a um recibo específico. Requisição exige autenticação com token e permissão dos perfis 'ADMIN' ou 'CLIENTE'.",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(
                            in = ParameterIn.PATH,
                            name = "recibo",
                            description = "Número do recibo a ser consultado",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Estacionamento encontrado com sucesso",
                            content = @Content(
                                    mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = EstacionamentoResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Acesso não permitido para o perfil do usuário",
                            content = @Content(
                                    mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Recibo não encontrado",
                            content = @Content(
                                    mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    )
            }
    )

    @GetMapping("/check-in/{recibo}")
    @PreAuthorize("hasAnyRole ('ADMIN', 'CLIENTE')")
    public ResponseEntity<EstacionamentoResponseDto> getByRecibo(@PathVariable String recibo) {
        ClienteVaga clienteVaga = clienteVagaService.buscarPorRecibo(recibo);
        EstacionamentoResponseDto dto = ClienteVagaMapper.toDto(clienteVaga);
        return ResponseEntity.ok(dto);

    }

    @Operation(
            summary = "Realizar o check-out do estacionamento",
            description = "Efetua o check-out de um estacionamento associado a um recibo específico. Requisição exige autenticação com token e permissão dos perfis 'ADMIN' ou 'CLIENTE'.",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(
                            in = ParameterIn.PATH,
                            name = "recibo",
                            description = "Número do recibo para realizar o check-out",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Check-out realizado com sucesso",
                            content = @Content(
                                    mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = EstacionamentoResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Acesso não permitido para o perfil do usuário",
                            content = @Content(
                                    mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Recibo não encontrado",
                            content = @Content(
                                    mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    )
            }
    )

    @PutMapping("/check-out/{recibo}")
    @PreAuthorize("hasAnyRole ('ADMIN', 'CLIENTE')")
    public ResponseEntity<EstacionamentoResponseDto> checkout(@PathVariable String recibo) {
        ClienteVaga clienteVaga = estacionamentoService.checkOut(recibo);
        EstacionamentoResponseDto dto = ClienteVagaMapper.toDto(clienteVaga);
        return ResponseEntity.ok(dto);

    }

    @Operation(
            summary = "Localizar os registros de estacionamentos do cliente por CPF",
            description = "Localizar os registros de estacionamentos do cliente por CPF. Requisição exige uso de um bearer token.",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "cpf",
                            description = "Nº do CPF referente ao cliente a ser consultado",
                            required = true),
                    @Parameter(in = ParameterIn.QUERY, name = "page",
                            description = "Representa a página retornada",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0"))),
                    @Parameter(in = ParameterIn.QUERY, name = "size",
                            description = "Representa o total de elementos por página",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "5"))),
                    @Parameter(in = ParameterIn.QUERY, name = "sort",
                            description = "Campo padrão de ordenação dataEntrada,asc",
                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "dataEntrada,asc")),
                            hidden = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Recurso localizado com sucesso",
                            content = @Content(
                                    mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = PageableDto.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Recurso não permitido ao perfil do CLIENTE",
                            content = @Content(
                                    mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            }
    )

    @GetMapping("/cpf/{cpf}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDto> getAllEstacionamentosPorCpf(@PathVariable String cpf,
                                                                   @PageableDefault(size = 5,
                                                                   sort = "dataEntrada",
                                                                   direction = Sort.Direction.ASC)
                                                                   Pageable pageable) {

        Page<ClienteVagaProjection> projection = clienteVagaService.buscarTodosPorClienteCpf(cpf, pageable);

        PageableDto dto = PageableMapper.toDto(projection);
        return ResponseEntity.ok(dto);

    }
}
