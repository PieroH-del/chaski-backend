package com.example.chaski_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpcionDetallePedidoDTO {
    private Long id;
    private Long detallePedidoId;
    private Long opcionId;
    private String opcionNombre;
    private BigDecimal precioExtraCobrado;
}

