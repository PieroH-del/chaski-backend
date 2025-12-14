package com.example.chaski_backend.mapper;

import com.example.chaski_backend.dto.PagoDTO;
import com.example.chaski_backend.model.Pago;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PagoMapper {

    @Mapping(source = "pedido.id", target = "pedidoId")
    PagoDTO toDto(Pago entity);


    @Mapping(source = "pedidoId", target = "pedido.id")
    Pago toEntity(PagoDTO dto);
}

