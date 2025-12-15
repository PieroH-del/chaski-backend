package com.example.chaski_backend.controller;

import com.example.chaski_backend.dto.CrearPagoDTO;
import com.example.chaski_backend.dto.PagoDTO;
import com.example.chaski_backend.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PagoController {

    private final PagoService pagoService;

    /**
     * Crea un nuevo pago simulado.
     * El pago se crea con estado PENDIENTE o COMPLETADO según la configuración.
     */
    @PostMapping
    public ResponseEntity<PagoDTO> crear(@Valid @RequestBody CrearPagoDTO dto) {
        PagoDTO pago = pagoService.crearPago(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pago);
    }

    /**
     * Confirma un pago pendiente manualmente.
     * Simula la aprobación del pago y actualiza el estado del pedido.
     */
    @PostMapping("/{id}/confirmar")
    public ResponseEntity<PagoDTO> confirmar(@PathVariable Long id) {
        PagoDTO pago = pagoService.confirmarPago(id);
        return ResponseEntity.ok(pago);
    }

    /**
     * Marca un pago como fallido.
     * Simula el rechazo del pago.
     */
    @PostMapping("/{id}/marcar-fallido")
    public ResponseEntity<PagoDTO> marcarComoFallido(@PathVariable Long id) {
        PagoDTO pago = pagoService.marcarComoFallido(id);
        return ResponseEntity.ok(pago);
    }

    /**
     * Obtiene el pago asociado a un pedido.
     */
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<PagoDTO> obtenerPorPedido(@PathVariable Long pedidoId) {
        PagoDTO pago = pagoService.obtenerPorPedido(pedidoId);
        return ResponseEntity.ok(pago);
    }

    /**
     * Obtiene un pago por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PagoDTO> obtenerPorId(@PathVariable Long id) {
        PagoDTO pago = pagoService.obtenerPorId(id);
        return ResponseEntity.ok(pago);
    }
}

