package com.example.chaski_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrupoOpcionesDTO {
    private Long id;
    private Long productoId;
    private String nombre;
    private Boolean esObligatorio;
    private Integer seleccionMinima;
    private Integer seleccionMaxima;
    private List<OpcionDTO> opciones;
}

