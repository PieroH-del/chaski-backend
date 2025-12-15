package com.example.chaski_backend.controller;

import com.example.chaski_backend.dto.ProductoDTO;
import com.example.chaski_backend.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<ProductoDTO> crear(@RequestBody ProductoDTO productoDTO) {
        ProductoDTO nuevoProducto = productoService.crear(productoDTO);
        return ResponseEntity.status(201).body(nuevoProducto);
    }

    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<List<ProductoDTO>> obtenerPorRestaurante(@PathVariable Long restauranteId) {
        List<ProductoDTO> productos = productoService.obtenerPorRestaurante(restauranteId);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/restaurante/{restauranteId}/disponibles")
    public ResponseEntity<List<ProductoDTO>> obtenerDisponiblesPorRestaurante(
            @PathVariable Long restauranteId) {
        List<ProductoDTO> productos = productoService.obtenerDisponiblesPorRestaurante(restauranteId);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerPorId(@PathVariable Long id) {
        ProductoDTO producto = productoService.obtenerPorId(id);
        return ResponseEntity.ok(producto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizar(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {
        ProductoDTO productoActualizado = productoService.actualizar(id, productoDTO);
        return ResponseEntity.ok(productoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

