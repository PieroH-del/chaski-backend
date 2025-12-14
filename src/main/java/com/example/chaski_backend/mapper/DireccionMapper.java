package com.example.chaski_backend.mapper;

import com.example.chaski_backend.dto.DireccionDTO;
import com.example.chaski_backend.model.Direccion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DireccionMapper {

    @Mapping(source = "usuario.id", target = "usuarioId")
    DireccionDTO toDto(Direccion entity);

    @Mapping(source = "usuarioId", target = "usuario.id")
    Direccion toEntity(DireccionDTO dto);
}

