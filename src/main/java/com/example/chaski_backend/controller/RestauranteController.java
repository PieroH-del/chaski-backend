package com.example.chaski_backend.controller;

import com.example.chaski_backend.dto.RestauranteDTO;
import com.example.chaski_backend.service.RestauranteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/restaurantes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RestauranteController {

    private final RestauranteService restauranteService;

    @PostMapping
    public ResponseEntity<RestauranteDTO> crear(@RequestBody RestauranteDTO restauranteDTO) {
        RestauranteDTO nuevoRestaurante = restauranteService.crear(restauranteDTO);
        return ResponseEntity.status(201).body(nuevoRestaurante);
    }

    @GetMapping
    public ResponseEntity<List<RestauranteDTO>> obtenerTodos() {
        List<RestauranteDTO> restaurantes = restauranteService.listarTodos();
        return ResponseEntity.ok(restaurantes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestauranteDTO> obtenerPorId(@PathVariable Long id) {
        RestauranteDTO restaurante = restauranteService.obtenerPorId(id);
        return ResponseEntity.ok(restaurante);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<RestauranteDTO>> buscarPorNombre(@RequestParam String nombre) {
        List<RestauranteDTO> restaurantes = restauranteService.buscarPorNombre(nombre);
        return ResponseEntity.ok(restaurantes);
    }

    @GetMapping("/filtrar/disponibilidad")
    public ResponseEntity<List<RestauranteDTO>> filtrarPorDisponibilidad(
            @RequestParam Boolean estaAbierto) {
        List<RestauranteDTO> restaurantes = restauranteService.obtenerPorEstado(estaAbierto);
        return ResponseEntity.ok(restaurantes);
    }

    @GetMapping("/filtrar/categoria/{categoriaId}")
    public ResponseEntity<List<RestauranteDTO>> filtrarPorCategoria(@PathVariable Long categoriaId) {
        List<RestauranteDTO> restaurantes = restauranteService.obtenerPorCategoria(categoriaId);
        return ResponseEntity.ok(restaurantes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestauranteDTO> actualizar(@PathVariable Long id, @RequestBody RestauranteDTO restauranteDTO) {
        RestauranteDTO restauranteActualizado = restauranteService.actualizar(id, restauranteDTO);
        return ResponseEntity.ok(restauranteActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        restauranteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

