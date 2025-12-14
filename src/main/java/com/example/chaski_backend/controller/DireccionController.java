package com.example.chaski_backend.controller;

import com.example.chaski_backend.dto.DireccionDTO;
import com.example.chaski_backend.service.DireccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/direcciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DireccionController {

    private final DireccionService direccionService;

    @PostMapping
    public ResponseEntity<DireccionDTO> crear(@Valid @RequestBody DireccionDTO dto) {
        DireccionDTO direccion = direccionService.crearDireccion(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(direccion);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<DireccionDTO>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        List<DireccionDTO> direcciones = direccionService.obtenerDireccionesPorUsuario(usuarioId);
        return ResponseEntity.ok(direcciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DireccionDTO> obtenerPorId(@PathVariable Long id) {
        DireccionDTO direccion = direccionService.obtenerPorId(id);
        return ResponseEntity.ok(direccion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DireccionDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody DireccionDTO dto) {
        DireccionDTO direccion = direccionService.actualizarDireccion(id, dto);
        return ResponseEntity.ok(direccion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        direccionService.eliminarDireccion(id);
        return ResponseEntity.noContent().build();
    }
}
