package com.julio.park_api.web.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteCreateDto {

    @NotBlank
    @Size(min = 5, max = 100)
    private String nome;
    @Size(min = 11, max = 11)
    @CPF
    private String cpf;
}
