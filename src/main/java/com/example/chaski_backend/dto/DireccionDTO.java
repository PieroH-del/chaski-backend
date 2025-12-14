package com.example.chaski_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DireccionDTO {
    private Long id;
    private Long usuarioId;
    private String etiqueta;
    private String direccionCompleta;
    private String referencia;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private Boolean esPredeterminada = false;
}

