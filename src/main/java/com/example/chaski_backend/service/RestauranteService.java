package com.example.chaski_backend.service;

import com.example.chaski_backend.dto.RestauranteDTO;
import com.example.chaski_backend.mapper.RestauranteMapper;
import com.example.chaski_backend.model.Restaurante;
import com.example.chaski_backend.repository.RestauranteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RestauranteService {

    private final RestauranteRepository restauranteRepository;
    private final RestauranteMapper restauranteMapper;

    public RestauranteDTO crear(RestauranteDTO dto) {
        Restaurante restaurante = restauranteMapper.toEntity(dto);
        Restaurante guardado = restauranteRepository.save(restaurante);
        return restauranteMapper.toDto(guardado);
    }

    public RestauranteDTO obtenerPorId(Long id) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante no encontrado"));
        return restauranteMapper.toDto(restaurante);
    }

    public List<RestauranteDTO> listarTodos() {
        return restauranteRepository.findAll().stream()
                .map(restauranteMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<RestauranteDTO> obtenerPorEstado(Boolean estaAbierto) {
        return restauranteRepository.findByEstaAbierto(estaAbierto).stream()
                .map(restauranteMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<RestauranteDTO> obtenerPorCategoria(Long categoriaId) {
        return restauranteRepository.findByCategoriaId(categoriaId).stream()
                .map(restauranteMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<RestauranteDTO> buscarPorNombre(String nombre) {
        return restauranteRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(restauranteMapper::toDto)
                .collect(Collectors.toList());
    }

    public RestauranteDTO actualizar(Long id, RestauranteDTO dto) {
        Restaurante existente = restauranteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante no encontrado"));

        existente.setNombre(dto.getNombre());
        existente.setDescripcion(dto.getDescripcion());
        existente.setImagenLogoUrl(dto.getImagenLogoUrl());
        existente.setImagenPortadaUrl(dto.getImagenPortadaUrl());
        existente.setDireccion(dto.getDireccion());
        existente.setCalificacionPromedio(dto.getCalificacionPromedio());
        existente.setEstaAbierto(dto.getEstaAbierto());
        existente.setTiempoEsperaMinutos(dto.getTiempoEsperaMinutos());
        existente.setCostoEnvioBase(dto.getCostoEnvioBase());

        Restaurante guardado = restauranteRepository.save(existente);
        return restauranteMapper.toDto(guardado);
    }

    public void eliminar(Long id) {
        if (!restauranteRepository.existsById(id)) {
            throw new IllegalArgumentException("Restaurante no encontrado");
        }
        restauranteRepository.deleteById(id);
    }
}

