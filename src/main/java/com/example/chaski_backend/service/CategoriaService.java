package com.example.chaski_backend.service;

import com.example.chaski_backend.dto.CategoriaDTO;
import com.example.chaski_backend.mapper.CategoriaMapper;
import com.example.chaski_backend.model.Categoria;
import com.example.chaski_backend.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    @Transactional(readOnly = true)
    public List<CategoriaDTO> listarTodas() {
        return categoriaRepository.findAll().stream()
                .map(categoriaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoriaDTO obtenerPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
        return categoriaMapper.toDto(categoria);
    }

    @Transactional
    public CategoriaDTO crear(CategoriaDTO dto) {
        Categoria categoria = categoriaMapper.toEntity(dto);
        Categoria guardada = categoriaRepository.save(categoria);
        return categoriaMapper.toDto(guardada);
    }

    @Transactional
    public CategoriaDTO actualizar(Long id, CategoriaDTO dto) {
        Categoria existente = categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

        existente.setNombre(dto.getNombre());
        existente.setImagenUrl(dto.getImagenUrl());

        Categoria guardada = categoriaRepository.save(existente);
        return categoriaMapper.toDto(guardada);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new IllegalArgumentException("Categoría no encontrada");
        }
        categoriaRepository.deleteById(id);
    }
}

