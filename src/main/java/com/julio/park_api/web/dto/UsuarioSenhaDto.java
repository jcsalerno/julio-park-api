package com.julio.park_api.web.dto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioSenhaDto {
    private String senhaAtual;
    private String novaSenha;
    private String confirmaSenha;
}
