package com.example.chaski_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpcionDTO {
    private Long id;
    private Long grupoOpcionId;
    private String nombre;
    private BigDecimal precioExtra;
}

