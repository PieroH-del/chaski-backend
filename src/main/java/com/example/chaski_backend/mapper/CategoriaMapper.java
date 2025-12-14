package com.example.chaski_backend.mapper;

import com.example.chaski_backend.dto.CategoriaDTO;
import com.example.chaski_backend.model.Categoria;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {
    CategoriaDTO toDto(Categoria entity);
    Categoria toEntity(CategoriaDTO dto);
}

