package com.example.chaski_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestauranteDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String imagenLogoUrl;
    private String imagenPortadaUrl;
    private String direccion;
    private BigDecimal calificacionPromedio;
    private Boolean estaAbierto;
    private Integer tiempoEsperaMinutos;
    private BigDecimal costoEnvioBase;
    private List<CategoriaDTO> categorias;
}

