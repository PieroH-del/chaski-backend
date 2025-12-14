package com.example.chaski_backend.dto;

import com.example.chaski_backend.enums.MetodoPago;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearPagoDTO {
    private Long pedidoId;
    private BigDecimal monto;
    private MetodoPago metodo;
}

