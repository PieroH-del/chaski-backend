package com.example.chaski_backend.mapper;

import com.example.chaski_backend.dto.UsuarioDTO;
import com.example.chaski_backend.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "passw", ignore = true)
    @Mapping(target = "direcciones", ignore = true)
    @Mapping(target = "pedidos", ignore = true)
    UsuarioDTO toDto(Usuario entity);

    @Mapping(target = "passw", ignore = true)
    @Mapping(target = "direcciones", ignore = true)
    @Mapping(target = "pedidos", ignore = true)
    Usuario toEntity(UsuarioDTO dto);
}

