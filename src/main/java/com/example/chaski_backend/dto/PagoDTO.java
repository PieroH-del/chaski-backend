package com.example.chaski_backend.dto;

import com.example.chaski_backend.enums.EstadoPago;
import com.example.chaski_backend.enums.MetodoPago;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoDTO {
    private Long id;
    private Long pedidoId;
    private BigDecimal monto;
    private MetodoPago metodo;
    private EstadoPago estado;
    private String referenciaPasarela;
    private LocalDateTime fechaPago;
}

