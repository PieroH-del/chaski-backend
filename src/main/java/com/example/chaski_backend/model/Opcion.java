package com.example.chaski_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "opciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Opcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_opcion_id", nullable = false)
    private GrupoOpciones grupoOpciones;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "precio_extra", precision = 10, scale = 2)
    private BigDecimal precioExtra = BigDecimal.ZERO;
}

