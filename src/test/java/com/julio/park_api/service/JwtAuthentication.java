package com.julio.park_api.service;

import com.julio.park_api.jwt.JwtToken;
import com.julio.park_api.web.dto.UsuarioLoginDto;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import java.util.function.Consumer;

public class JwtAuthentication {

    public static Consumer<HttpHeaders> getHeaderAuthorization(WebTestClient client, String username, String password) {
        String token = client
                .post()
                .uri("/api/v1/auth")
                .bodyValue(new UsuarioLoginDto(username, password))
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtToken.class)
                .returnResult().getResponseBody().getToken();
        return headres -> headres.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

    }
}
