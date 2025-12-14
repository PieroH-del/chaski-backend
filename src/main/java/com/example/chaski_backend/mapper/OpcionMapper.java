package com.example.chaski_backend.mapper;

import com.example.chaski_backend.dto.OpcionDTO;
import com.example.chaski_backend.model.Opcion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OpcionMapper {

    @Mapping(source = "grupoOpciones.id", target = "grupoOpcionId")
    OpcionDTO toDto(Opcion entity);

    @Mapping(source = "grupoOpcionId", target = "grupoOpciones.id")
    Opcion toEntity(OpcionDTO dto);
}

