package com.julio.park_api.service;
import com.julio.park_api.web.dto.UsuarioCreateDto;
import com.julio.park_api.web.dto.UsuarioSenhaDto;
import com.julio.park_api.web.dto.mapper.UsuarioResponseDto;
import com.julio.park_api.web.exception.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/usuarios/usuarios-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/usuarios/usuarios-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UsuarioIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void createUsuario_ComUserNameEPasswordValidos_RetornarUsuarioCriadoComStatus201() {
        UsuarioResponseDto responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("tody@gmail.com", "123456"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UsuarioResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("tody@gmail.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENTE");
    }

    @ParameterizedTest
    @MethodSource(value = "invalidUsernames")
    public void createUsuario_ComUserNameInvalido_RetornarErrorMessageStatus422(String username) {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto(username, "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    private static Stream<Arguments> invalidUsernames() {
        return Stream.of(
                Arguments.of(""),
                Arguments.of("tody@"),
                Arguments.of("tody@email")
        );
    }

    @ParameterizedTest
    @MethodSource(value = "invalidPasswords")
    public void createUsuario_ComSenhaInvalida_RetornarErrorMessageStatus422(String password) {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("tody@gmail.com", password))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    private static Stream<Arguments> invalidPasswords() {
        return Stream.of(
                Arguments.of("12345"),
                Arguments.of("1234567"),
                Arguments.of("abcd")
        );
    }

    @ParameterizedTest
    @MethodSource(value = "duplicateUsernames")
    public void createUsuario_ComUserNameRepetido_RetornarErrorMessageStatus409(String username) {

        testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto(username, "123456"))
                .exchange()
                .expectStatus().isCreated();


        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto(username, "123456"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
    }

    private static Stream<Arguments> duplicateUsernames() {
        return Stream.of(
                Arguments.of("tati@teste.com.br"),
                Arguments.of("tati@teste.com.br")
        );
    }

    @ParameterizedTest
    @MethodSource(value = "userIds")
    public void buscarUsuarioPorId_RetornarUsuarioComStatus200(Long userId) {

        testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("tody@gmail.com", "123456"))
                .exchange()
                .expectStatus().isCreated();

        UsuarioResponseDto responseBody = testClient
                .get()
                .uri("/api/v1/usuarios/{id}", userId)
                .exchange()
                .expectStatus().isOk()  // Espera o status 200
                .expectBody(UsuarioResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(userId);
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("tody@gmail.com");
    }

    private static Stream<Arguments> userIds() {
        return Stream.of(
                Arguments.of(1L),
                Arguments.of(2L)
        );
    }


    @Test
    public void editarSenha_ComDadosValidos_RetornarStatus204() {
        testClient
                .patch()
                .uri("/api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("123456", "123456", "123456"))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void editarSenha_ComIdInexistente_RetornarErrorMessageComStatus404() {
        ErrorMessage responseBody = testClient
                .patch()
                .uri("/api/v1/usuarios/0")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("123456", "123456", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);

    }

    @Test
    public void editarSenha_ComCamposInvalidos_RetornarErrorMessageComStatus422() {
        ErrorMessage responseBody = testClient
                .patch()
                .uri("/api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("", "", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    }

    @ParameterizedTest
    @MethodSource(value = "invalidPasswords")
    public void editarSenha_ComSenhaInvalida_RetornarErrorMessageStatus422(String password) {
        ErrorMessage responseBody = testClient
                .patch()
                .uri("/api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("tody@gmail.com", password, password))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

@Test
    public void retornarTodos_OsUsuarios_DaLista() {

    List<UsuarioResponseDto> usuarioResponseDto = testClient
            .get()
            .uri("/api/v1/usuarios")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(UsuarioResponseDto.class)
            .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(usuarioResponseDto).isNotNull();
    Assertions.assertThat(usuarioResponseDto.size()).isEqualTo(3);

}

}

