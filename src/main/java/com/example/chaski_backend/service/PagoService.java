package com.example.chaski_backend.service;

import com.example.chaski_backend.dto.CrearPagoDTO;
import com.example.chaski_backend.dto.PagoDTO;
import com.example.chaski_backend.enums.EstadoPago;
import com.example.chaski_backend.enums.EstadoPedido;
import com.example.chaski_backend.enums.MetodoPago;
import com.example.chaski_backend.mapper.PagoMapper;
import com.example.chaski_backend.model.Pago;
import com.example.chaski_backend.model.Pedido;
import com.example.chaski_backend.repository.PagoRepository;
import com.example.chaski_backend.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;
    private final PedidoRepository pedidoRepository;
    private final PagoMapper pagoMapper;

    @Value("${payment.auto.approve:false}")
    private boolean autoApprovePayments;

    /**
     * Crea un pago simulado sin integración con Stripe.
     * Todos los pagos se crean con referencias ficticias.
     */
    @Transactional
    public PagoDTO crearPago(CrearPagoDTO dto) {
        Pedido pedido = pedidoRepository.findById(dto.getPedidoId())
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // Validar que el pedido no tenga ya un pago
        if (pagoRepository.findByPedidoId(dto.getPedidoId()).isPresent()) {
            throw new RuntimeException("El pedido ya tiene un pago asociado");
        }

        Pago pago = new Pago();
        pago.setPedido(pedido);
        pago.setMonto(dto.getMonto());
        pago.setMetodo(dto.getMetodo());

        // Generar referencia ficticia para todos los métodos de pago
        String referenciaSimulada = generarReferenciaSimulada(dto.getMetodo());
        pago.setReferenciaPasarela(referenciaSimulada);

        // Si auto-aprobación está activada, marcar como completado automáticamente
        if (autoApprovePayments) {
            pago.setEstado(EstadoPago.COMPLETADO);
            pago.setFechaPago(LocalDateTime.now());

            // Actualizar estado del pedido
            pedido.setEstado(EstadoPedido.CONFIRMADO_TIENDA);
            pedidoRepository.save(pedido);
        } else {
            // Por defecto, crear como PENDIENTE
            pago.setEstado(EstadoPago.PENDIENTE);
        }

        Pago pagoGuardado = pagoRepository.save(pago);
        return pagoMapper.toDto(pagoGuardado);
    }

    /**
     * Confirma un pago pendiente de forma manual.
     * Simula la aprobación del pago y actualiza el estado del pedido.
     */
    @Transactional
    public PagoDTO confirmarPago(Long pagoId) {
        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        if (pago.getEstado() == EstadoPago.COMPLETADO) {
            throw new RuntimeException("El pago ya fue completado");
        }

        pago.setEstado(EstadoPago.COMPLETADO);
        pago.setFechaPago(LocalDateTime.now());

        // Actualizar estado del pedido a CONFIRMADO_TIENDA
        Pedido pedido = pago.getPedido();
        pedido.setEstado(EstadoPedido.CONFIRMADO_TIENDA);
        pedidoRepository.save(pedido);

        Pago pagoActualizado = pagoRepository.save(pago);
        return pagoMapper.toDto(pagoActualizado);
    }

    /**
     * Marca un pago como fallido.
     * Simula el rechazo del pago.
     */
    @Transactional
    public PagoDTO marcarComoFallido(Long pagoId) {
        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        pago.setEstado(EstadoPago.FALLIDO);

        Pago pagoActualizado = pagoRepository.save(pago);
        return pagoMapper.toDto(pagoActualizado);
    }

    /**
     * Obtiene el pago asociado a un pedido.
     */
    @Transactional(readOnly = true)
    public PagoDTO obtenerPorPedido(Long pedidoId) {
        Pago pago = pagoRepository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado para este pedido"));
        return pagoMapper.toDto(pago);
    }

    /**
     * Obtiene un pago por su ID.
     */
    @Transactional(readOnly = true)
    public PagoDTO obtenerPorId(Long pagoId) {
        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        return pagoMapper.toDto(pago);
    }

    /**
     * Genera una referencia simulada para el pago.
     * Formato: METODO-TIMESTAMP-RANDOM
     * Ejemplos:
     * - TARJETA-1702564321-A3F9
     * - YAPE-1702564322-B7C1
     * - EFECTIVO-1702564323-D2E8
     */
    private String generarReferenciaSimulada(MetodoPago metodo) {
        long timestamp = System.currentTimeMillis() / 1000; // Timestamp en segundos
        String randomCode = generarCodigoAleatorio(4);

        String prefijo = switch (metodo) {
            case TARJETA_CREDITO -> "TC";
            case TARJETA_DEBITO -> "TD";
            case YAPE -> "YAPE";
            case EFECTIVO -> "EFEC";
        };

        return String.format("SIM-%s-%d-%s", prefijo, timestamp, randomCode);
    }

    /**
     * Genera un código alfanumérico aleatorio.
     */
    private String generarCodigoAleatorio(int longitud) {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder codigo = new StringBuilder();

        for (int i = 0; i < longitud; i++) {
            codigo.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }

        return codigo.toString();
    }
}

