package com.example.chaski_backend.controller;

import com.example.chaski_backend.dto.CrearPagoDTO;
import com.example.chaski_backend.dto.PagoDTO;
import com.example.chaski_backend.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PagoController {

    private final PagoService pagoService;

    @PostMapping
    public ResponseEntity<PagoDTO> crear(@Valid @RequestBody CrearPagoDTO dto) {
        PagoDTO pago = pagoService.crearPago(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pago);
    }

    @PostMapping("/{id}/confirmar")
    public ResponseEntity<PagoDTO> confirmar(@PathVariable Long id) {
        PagoDTO pago = pagoService.confirmarPago(id);
        return ResponseEntity.ok(pago);
    }

    @PostMapping("/{id}/marcar-fallido")
    public ResponseEntity<PagoDTO> marcarComoFallido(@PathVariable Long id) {
        PagoDTO pago = pagoService.marcarComoFallido(id);
        return ResponseEntity.ok(pago);
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<PagoDTO> obtenerPorPedido(@PathVariable Long pedidoId) {
        PagoDTO pago = pagoService.obtenerPorPedido(pedidoId);
        return ResponseEntity.ok(pago);
    }

    @GetMapping("/{id}/client-secret")
    public ResponseEntity<Map<String, String>> obtenerClientSecret(@PathVariable Long id) {
        Map<String, String> response = pagoService.obtenerClientSecret(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/webhook/stripe")
    public ResponseEntity<Void> webhookStripe(@RequestBody Map<String, Object> payload) {
        pagoService.procesarWebhookStripe(payload);
        return ResponseEntity.ok().build();
    }
}

