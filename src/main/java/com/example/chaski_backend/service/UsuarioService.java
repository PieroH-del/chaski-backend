package com.example.chaski_backend.service;

import com.example.chaski_backend.dto.LoginDTO;
import com.example.chaski_backend.dto.UsuarioActualizacionDTO;
import com.example.chaski_backend.dto.UsuarioDTO;
import com.example.chaski_backend.dto.UsuarioRegistroDTO;
import com.example.chaski_backend.mapper.UsuarioMapper;
import com.example.chaski_backend.model.Usuario;
import com.example.chaski_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UsuarioDTO registrar(UsuarioRegistroDTO dto) {
        // Verificar si ya existe el email
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPassw(passwordEncoder.encode(dto.getPassword()));
        usuario.setTelefono(dto.getTelefono());
        usuario.setImagenPerfilUrl(dto.getImagenPerfilUrl()); // Puede ser null
        usuario.setActivo(true);

        Usuario guardado = usuarioRepository.save(usuario);
        return usuarioMapper.toDto(guardado);
    }

    public UsuarioDTO login(LoginDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassw())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        if (!usuario.getActivo()) {
            throw new IllegalArgumentException("Usuario inactivo");
        }

        return usuarioMapper.toDto(usuario);
    }

    public UsuarioDTO obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return usuarioMapper.toDto(usuario);
    }

    public UsuarioDTO actualizar(Long id, UsuarioActualizacionDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (dto.getNombre() != null) {
            usuario.setNombre(dto.getNombre());
        }
        if (dto.getTelefono() != null) {
            usuario.setTelefono(dto.getTelefono());
        }
        if (dto.getImagenPerfilUrl() != null) {
            usuario.setImagenPerfilUrl(dto.getImagenPerfilUrl());
        }

        Usuario guardado = usuarioRepository.save(usuario);
        return usuarioMapper.toDto(guardado);
    }

    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toDto)
                .collect(Collectors.toList());
    }

    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }
}

