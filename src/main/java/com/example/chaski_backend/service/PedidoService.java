package com.example.chaski_backend.service;

import com.example.chaski_backend.dto.DetallePedidoDTO;
import com.example.chaski_backend.dto.OpcionDetallePedidoDTO;
import com.example.chaski_backend.dto.PedidoDTO;
import com.example.chaski_backend.enums.EstadoPedido;
import com.example.chaski_backend.mapper.PedidoMapper;
import com.example.chaski_backend.model.*;
import com.example.chaski_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final RestauranteRepository restauranteRepository;
    private final DireccionRepository direccionRepository;
    private final ProductoRepository productoRepository;
    private final PedidoMapper pedidoMapper;
    private final OpcionRepository opcionRepository;

    private static final BigDecimal TASA_IMPUESTO = new BigDecimal("0.18"); // 18% IGV

    @Transactional
    public PedidoDTO crearPedido(PedidoDTO dto) {
        // Validar existencia de entidades
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));

        Direccion direccion = direccionRepository.findById(dto.getDireccionEntregaId())
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));

        // Validar que el restaurante esté abierto
        if (!restaurante.getEstaAbierto()) {
            throw new RuntimeException("El restaurante está cerrado");
        }

        // Crear pedido
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setRestaurante(restaurante);
        pedido.setDireccionEntrega(direccion);
        pedido.setNotasInstrucciones(dto.getNotasInstrucciones());
        pedido.setEstado(EstadoPedido.PENDIENTE_PAGO);

        // Procesar detalles
        List<DetallePedido> detalles = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (DetallePedidoDTO detalleDTO : dto.getDetalles()) {
            Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + detalleDTO.getProductoId()));

            if (!producto.getDisponible()) {
                throw new RuntimeException("Producto no disponible: " + producto.getNombre());
            }

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());

            // Procesar opciones
            List<OpcionDetallePedido> opciones = new ArrayList<>();
            BigDecimal precioOpciones = BigDecimal.ZERO;

            if (detalleDTO.getOpciones() != null) {
                for (OpcionDetallePedidoDTO opcionDTO : detalleDTO.getOpciones()) {
                    Opcion opcion = opcionRepository.findById(opcionDTO.getOpcionId())
                            .orElseThrow(() -> new RuntimeException("Opción no encontrada: " + opcionDTO.getOpcionId()));

                    OpcionDetallePedido opcionDetalle = new OpcionDetallePedido();
                    opcionDetalle.setDetallePedido(detalle);
                    opcionDetalle.setOpcion(opcion);
                    opcionDetalle.setPrecioExtraCobrado(opcion.getPrecioExtra());
                    opciones.add(opcionDetalle);

                    precioOpciones = precioOpciones.add(opcion.getPrecioExtra());
                }
            }

            detalle.setOpciones(opciones);
            detalles.add(detalle);

            // Calcular subtotal: (precio producto + precio opciones) * cantidad
            BigDecimal precioTotal = producto.getPrecio().add(precioOpciones);
            subtotal = subtotal.add(precioTotal.multiply(new BigDecimal(detalleDTO.getCantidad())));
        }

        pedido.setDetalles(detalles);
        pedido.setSubtotalProductos(subtotal);
        pedido.setCostoEnvio(restaurante.getCostoEnvioBase());
        pedido.setImpuestos(subtotal.multiply(TASA_IMPUESTO));
        pedido.setTotalFinal(subtotal.add(pedido.getCostoEnvio()).add(pedido.getImpuestos()));

        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        return pedidoMapper.toDto(pedidoGuardado);
    }

    @Transactional(readOnly = true)
    public List<PedidoDTO> obtenerPorUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId).stream()
                .map(pedidoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PedidoDTO obtenerPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        return pedidoMapper.toDto(pedido);
    }

    @Transactional
    public PedidoDTO actualizarEstado(Long id, EstadoPedido nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // Validar transición de estados
        validarTransicionEstado(pedido.getEstado(), nuevoEstado);

        pedido.setEstado(nuevoEstado);
        Pedido pedidoActualizado = pedidoRepository.save(pedido);

        return pedidoMapper.toDto(pedidoActualizado);
    }

    @Transactional
    public PedidoDTO cancelarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // Solo se puede cancelar si está en ciertos estados
        if (pedido.getEstado() == EstadoPedido.EN_CAMINO ||
            pedido.getEstado() == EstadoPedido.ENTREGADO ||
            pedido.getEstado() == EstadoPedido.CANCELADO) {
            throw new RuntimeException("No se puede cancelar el pedido en su estado actual");
        }

        pedido.setEstado(EstadoPedido.CANCELADO);
        Pedido pedidoActualizado = pedidoRepository.save(pedido);

        return pedidoMapper.toDto(pedidoActualizado);
    }

    @Transactional(readOnly = true)
    public List<PedidoDTO> obtenerPorEstado(EstadoPedido estado) {
        return pedidoRepository.findByEstado(estado).stream()
                .map(pedidoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void eliminar(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new IllegalArgumentException("Pedido no encontrado");
        }
        pedidoRepository.deleteById(id);
    }

    private void validarTransicionEstado(EstadoPedido estadoActual, EstadoPedido nuevoEstado) {
        // Validación básica de flujo de estados
        if (estadoActual == EstadoPedido.CANCELADO || estadoActual == EstadoPedido.ENTREGADO) {
            throw new RuntimeException("No se puede cambiar el estado de un pedido cancelado o entregado");
        }

        // Validar secuencia lógica
        int ordenActual = getOrdenEstado(estadoActual);
        int ordenNuevo = getOrdenEstado(nuevoEstado);

        if (nuevoEstado != EstadoPedido.CANCELADO && ordenNuevo < ordenActual) {
            throw new RuntimeException("Transición de estado inválida");
        }
    }

    private int getOrdenEstado(EstadoPedido estado) {
        return switch (estado) {
            case PENDIENTE_PAGO -> 1;
            case CONFIRMADO_TIENDA -> 2;
            case EN_PREPARACION -> 3;
            case LISTO_PARA_RECOGER -> 4;
            case EN_CAMINO -> 5;
            case ENTREGADO -> 6;
            case CANCELADO -> 0;
        };
    }
}

