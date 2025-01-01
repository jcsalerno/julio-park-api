package com.julio.park_api.web.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstacionamentoResponseDto {

    private String placa;
    private String marca;
    private String modelo;
    private String cor;
    private String clienteCpf;
    private String recibo;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;
    private String VagaCodigo;
    private BigDecimal valor;
    private BigDecimal desconto;
}
