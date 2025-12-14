package com.example.chaski_backend.service;

import com.example.chaski_backend.dto.DireccionDTO;
import com.example.chaski_backend.mapper.DireccionMapper;
import com.example.chaski_backend.model.Direccion;
import com.example.chaski_backend.model.Usuario;
import com.example.chaski_backend.repository.DireccionRepository;
import com.example.chaski_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DireccionService {

    private final DireccionRepository direccionRepository;
    private final UsuarioRepository usuarioRepository;
    private final DireccionMapper direccionMapper;

    public DireccionDTO crearDireccion(DireccionDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Direccion direccion = direccionMapper.toEntity(dto);
        direccion.setUsuario(usuario);
        Direccion guardada = direccionRepository.save(direccion);
        return direccionMapper.toDto(guardada);
    }

    public List<DireccionDTO> obtenerDireccionesPorUsuario(Long usuarioId) {
        List<Direccion> direcciones = direccionRepository.findByUsuarioId(usuarioId);
        return direcciones.stream().map(direccionMapper::toDto).collect(Collectors.toList());
    }

    public DireccionDTO obtenerPorId(Long id) {
        Direccion direccion = direccionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Direccion no encontrada"));
        return direccionMapper.toDto(direccion);
    }

    public DireccionDTO actualizarDireccion(Long id, DireccionDTO dto) {
        Direccion existente = direccionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Direccion no encontrada"));

        // actualizar campos básicos
        existente.setEtiqueta(dto.getEtiqueta());
        existente.setDireccionCompleta(dto.getDireccionCompleta());
        existente.setReferencia(dto.getReferencia());
        existente.setLatitud(dto.getLatitud());
        existente.setLongitud(dto.getLongitud());
        existente.setEsPredeterminada(dto.getEsPredeterminada());

        Direccion guardada = direccionRepository.save(existente);
        return direccionMapper.toDto(guardada);
    }

    public void eliminarDireccion(Long id) {
        if (!direccionRepository.existsById(id)) {
            throw new IllegalArgumentException("Direccion no encontrada");
        }
        direccionRepository.deleteById(id);
    }
}

