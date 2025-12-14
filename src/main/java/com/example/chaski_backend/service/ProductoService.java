package com.example.chaski_backend.service;

import com.example.chaski_backend.dto.ProductoDTO;
import com.example.chaski_backend.mapper.ProductoMapper;
import com.example.chaski_backend.model.Producto;
import com.example.chaski_backend.model.Restaurante;
import com.example.chaski_backend.repository.ProductoRepository;
import com.example.chaski_backend.repository.RestauranteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final RestauranteRepository restauranteRepository;
    private final ProductoMapper productoMapper;

    public ProductoDTO crear(ProductoDTO dto) {
        Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurante no encontrado"));

        Producto producto = productoMapper.toEntity(dto);
        producto.setRestaurante(restaurante);
        Producto guardado = productoRepository.save(producto);
        return productoMapper.toDto(guardado);
    }

    public ProductoDTO obtenerPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        return productoMapper.toDto(producto);
    }

    public List<ProductoDTO> obtenerPorRestaurante(Long restauranteId) {
        return productoRepository.findByRestauranteId(restauranteId).stream()
                .map(productoMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ProductoDTO> obtenerDisponiblesPorRestaurante(Long restauranteId) {
        return productoRepository.findByRestauranteIdAndDisponible(restauranteId, true).stream()
                .map(productoMapper::toDto)
                .collect(Collectors.toList());
    }

    public ProductoDTO actualizar(Long id, ProductoDTO dto) {
        Producto existente = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        existente.setNombre(dto.getNombre());
        existente.setDescripcion(dto.getDescripcion());
        existente.setPrecio(dto.getPrecio());
        existente.setImagenUrl(dto.getImagenUrl());
        existente.setDisponible(dto.getDisponible());

        Producto guardado = productoRepository.save(existente);
        return productoMapper.toDto(guardado);
    }

    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new IllegalArgumentException("Producto no encontrado");
        }
        productoRepository.deleteById(id);
    }

    public List<ProductoDTO> listarTodos() {
        return productoRepository.findAll().stream()
                .map(productoMapper::toDto)
                .collect(Collectors.toList());
    }
}

