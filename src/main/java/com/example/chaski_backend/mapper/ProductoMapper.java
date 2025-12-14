package com.example.chaski_backend.mapper;

import com.example.chaski_backend.dto.ProductoDTO;
import com.example.chaski_backend.model.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {GrupoOpcionesMapper.class})
public interface ProductoMapper {

    @Mapping(source = "restaurante.id", target = "restauranteId")
    ProductoDTO toDto(Producto entity);

    @Mapping(source = "restauranteId", target = "restaurante.id")
    Producto toEntity(ProductoDTO dto);
}

