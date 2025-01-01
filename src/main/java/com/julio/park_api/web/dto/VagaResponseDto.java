package com.julio.park_api.web.dto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class VagaResponseDto {

    private Long id;
    private String codigo;
    private String status;
}
