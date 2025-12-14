package com.example.chaski_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurantes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "imagen_logo_url")
    private String imagenLogoUrl;

    @Column(name = "imagen_portada_url")
    private String imagenPortadaUrl;

    private String direccion;

    @Column(name = "calificacion_promedio", precision = 3, scale = 2)
    private BigDecimal calificacionPromedio = BigDecimal.ZERO;

    @Column(name = "esta_abierto")
    private Boolean estaAbierto = true;

    @Column(name = "tiempo_espera_minutos")
    private Integer tiempoEsperaMinutos;

    @Column(name = "costo_envio_base", precision = 10, scale = 2)
    private BigDecimal costoEnvioBase;

    @ManyToMany
    @JoinTable(
        name = "restaurante_categorias",
        joinColumns = @JoinColumn(name = "restaurante_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private List<Categoria> categorias = new ArrayList<>();

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL)
    private List<Producto> productos = new ArrayList<>();

    @OneToMany(mappedBy = "restaurante")
    private List<Pedido> pedidos = new ArrayList<>();
}

