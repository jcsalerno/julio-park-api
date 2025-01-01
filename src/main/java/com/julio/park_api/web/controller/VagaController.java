package com.julio.park_api.web.controller;
import com.julio.park_api.entity.Vaga;
import com.julio.park_api.service.VagaService;
import com.julio.park_api.web.dto.VagaCreateDto;
import com.julio.park_api.web.dto.VagaResponseDto;
import com.julio.park_api.web.dto.mapper.UsuarioResponseDto;
import com.julio.park_api.web.dto.mapper.VagaMapper;
import com.julio.park_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
@Tag(name = "Vagas", description = "Operações de registro de entrada e saída de uma vaga")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/vagas")
public class VagaController {

    private final VagaService vagaService;

    @Operation(summary = "Criar uma nova vaga",
            description = "Recurso para criar uma nova vaga" +
            "Requisição exige uso de um Bearer Token. Acesso restrito a Role='ADMIN'",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                            headers = @Header(name = HttpHeaders.LOCATION, description = "URL do recurso criado")),

                    @ApiResponse(responseCode = "409", description = "Vaga já cadastrada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),

                    @ApiResponse(responseCode = "422",
                            description = "Recurso não processado por falta de dados ou dados inválidos",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),

                    @ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil do cliente",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
            })

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(
            @Valid
            @Parameter(description = "DTO para criar uma nova vaga", required = true)
            @RequestBody VagaCreateDto dto) {
        Vaga vaga = VagaMapper.toVaga(dto);
        vagaService.salvar(vaga);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{codigo}")
                .buildAndExpand(vaga.getCodigo())
                .toUri();

        return ResponseEntity.created(location).build();

    }

    @Operation(summary = "Localizar uma vaga",
              description = "Recurso para retornar uma vaga pelo seu código" +
                      "Requisição exige uso de um Bearer Token. Acesso restrito a Role='ADMIN'",
            security = @SecurityRequirement(name = "security"),
              responses = {
        @ApiResponse(responseCode = "200", description = "Recurso criado com sucesso",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = VagaResponseDto.class))),

        @ApiResponse(responseCode = "404", description = "Vaga não localizada",
                content = @Content(mediaType = "application/json;charset=UTF-8",
                        schema = @Schema(implementation = ErrorMessage.class))),

        @ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil do cliente",
                              content = @Content(mediaType = "application/json;charset=UTF-8",
                                      schema = @Schema(implementation = ErrorMessage.class))),


              })

    @GetMapping("/{codigo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VagaResponseDto> getByCodigo(@PathVariable String codigo) {
        Vaga vaga = vagaService.buscarPorCodigo(codigo);
        return ResponseEntity.ok(VagaMapper.toDto(vaga));
    }

}
