package com.example.chaski_backend.mapper;

import com.example.chaski_backend.dto.RestauranteDTO;
import com.example.chaski_backend.model.Restaurante;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CategoriaMapper.class})
public interface RestauranteMapper {
    RestauranteDTO toDto(Restaurante entity);
    Restaurante toEntity(RestauranteDTO dto);
}

