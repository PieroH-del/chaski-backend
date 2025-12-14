package com.example.chaski_backend.mapper;

import com.example.chaski_backend.dto.GrupoOpcionesDTO;
import com.example.chaski_backend.model.GrupoOpciones;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OpcionMapper.class})
public interface GrupoOpcionesMapper {

    @Mapping(source = "producto.id", target = "productoId")
    GrupoOpcionesDTO toDto(GrupoOpciones entity);

    @Mapping(source = "productoId", target = "producto.id")
    GrupoOpciones toEntity(GrupoOpcionesDTO dto);
}

