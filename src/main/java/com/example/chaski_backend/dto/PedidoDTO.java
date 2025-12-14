package com.example.chaski_backend.dto;

import com.example.chaski_backend.enums.EstadoPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    private Long id;
    private Long usuarioId;
    private Long restauranteId;
    private Long direccionEntregaId;
    private BigDecimal subtotalProductos;
    private BigDecimal costoEnvio;
    private BigDecimal impuestos;
    private BigDecimal totalFinal;
    private EstadoPedido estado;
    private String notasInstrucciones;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private List<DetallePedidoDTO> detalles;
}

