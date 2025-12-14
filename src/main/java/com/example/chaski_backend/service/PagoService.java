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
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;
    private final PedidoRepository pedidoRepository;
    private final PagoMapper pagoMapper;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

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
        pago.setEstado(EstadoPago.PENDIENTE);

        // Si es efectivo, marcar como pendiente
        if (dto.getMetodo() == MetodoPago.EFECTIVO) {
            pago.setReferenciaPasarela("EFECTIVO");
        } else {
            // Para otros métodos, crear intención de pago con Stripe
            try {
                String paymentIntentId = crearIntencionPagoStripe(dto.getMonto().longValue());
                pago.setReferenciaPasarela(paymentIntentId);
            } catch (StripeException e) {
                throw new RuntimeException("Error al crear la intención de pago: " + e.getMessage());
            }
        }

        Pago pagoGuardado = pagoRepository.save(pago);
        return pagoMapper.toDto(pagoGuardado);
    }

    @Transactional
    public PagoDTO confirmarPago(Long pagoId) {
        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        if (pago.getEstado() == EstadoPago.COMPLETADO) {
            throw new RuntimeException("El pago ya fue completado");
        }

        pago.setEstado(EstadoPago.COMPLETADO);
        pago.setFechaPago(LocalDateTime.now());

        // Actualizar estado del pedido
        Pedido pedido = pago.getPedido();
        pedido.setEstado(EstadoPedido.CONFIRMADO_TIENDA);
        pedidoRepository.save(pedido);

        Pago pagoActualizado = pagoRepository.save(pago);
        return pagoMapper.toDto(pagoActualizado);
    }

    @Transactional
    public PagoDTO marcarComoFallido(Long pagoId) {
        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        pago.setEstado(EstadoPago.FALLIDO);

        Pago pagoActualizado = pagoRepository.save(pago);
        return pagoMapper.toDto(pagoActualizado);
    }

    @Transactional(readOnly = true)
    public PagoDTO obtenerPorPedido(Long pedidoId) {
        Pago pago = pagoRepository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado para este pedido"));
        return pagoMapper.toDto(pago);
    }

    @Transactional(readOnly = true)
    public PagoDTO obtenerPorReferencia(String referenciaPasarela) {
        Pago pago = pagoRepository.findByReferenciaPasarela(referenciaPasarela)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        return pagoMapper.toDto(pago);
    }

    @Transactional
    public PagoDTO procesarWebhookStripe(Map<String, Object> payload) {
        // Procesar webhook de Stripe
        String paymentIntentId = (String) payload.get("id");
        String status = (String) payload.get("status");

        Pago pago = pagoRepository.findByReferenciaPasarela(paymentIntentId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        switch (status) {
            case "succeeded":
                pago.setEstado(EstadoPago.COMPLETADO);
                pago.setFechaPago(LocalDateTime.now());

                // Actualizar estado del pedido
                Pedido pedido = pago.getPedido();
                pedido.setEstado(EstadoPedido.CONFIRMADO_TIENDA);
                pedidoRepository.save(pedido);
                break;

            case "payment_failed":
                pago.setEstado(EstadoPago.FALLIDO);
                break;

            case "canceled":
                pago.setEstado(EstadoPago.FALLIDO);
                break;
        }

        Pago pagoGuardado = pagoRepository.save(pago);
        return pagoMapper.toDto(pagoGuardado);
    }

    private String crearIntencionPagoStripe(Long monto) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        // Stripe maneja los montos en centavos
        long montoCentavos = monto * 100;

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(montoCentavos)
                .setCurrency("pen") // Soles peruanos
                .addPaymentMethodType("card")
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        return paymentIntent.getId();
    }

    public Map<String, String> obtenerClientSecret(Long pagoId) {
        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        try {
            Stripe.apiKey = stripeApiKey;
            PaymentIntent paymentIntent = PaymentIntent.retrieve(pago.getReferenciaPasarela());

            Map<String, String> response = new HashMap<>();
            response.put("clientSecret", paymentIntent.getClientSecret());
            response.put("paymentIntentId", paymentIntent.getId());

            return response;
        } catch (StripeException e) {
            throw new RuntimeException("Error al obtener el client secret: " + e.getMessage());
        }
    }
}

