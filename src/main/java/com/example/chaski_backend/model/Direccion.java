package com.example.chaski_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "direcciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private String etiqueta;

    @Column(name = "direccion_completa", nullable = false)
    private String direccionCompleta;

    private String referencia;

    private BigDecimal latitud;

    private BigDecimal longitud;

    @Column(name = "es_predeterminada")
    private Boolean esPredeterminada = false;
}

