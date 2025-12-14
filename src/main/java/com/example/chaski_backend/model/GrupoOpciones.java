package com.example.chaski_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "grupos_opciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrupoOpciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "es_obligatorio")
    private Boolean esObligatorio = false;

    @Column(name = "seleccion_minima")
    private Integer seleccionMinima = 0;

    @Column(name = "seleccion_maxima")
    private Integer seleccionMaxima = 1;

    @OneToMany(mappedBy = "grupoOpciones", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Opcion> opciones = new ArrayList<>();
}

