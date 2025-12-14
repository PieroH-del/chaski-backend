package com.example.chaski_backend.mapper;

import com.example.chaski_backend.dto.OpcionDetallePedidoDTO;
import com.example.chaski_backend.model.OpcionDetallePedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OpcionDetallePedidoMapper {

    @Mapping(source = "detallePedido.id", target = "detallePedidoId")
    @Mapping(source = "opcion.id", target = "opcionId")
    @Mapping(source = "opcion.nombre", target = "opcionNombre")
    OpcionDetallePedidoDTO toDto(OpcionDetallePedido entity);

    @Mapping(source = "detallePedidoId", target = "detallePedido.id")
    @Mapping(source = "opcionId", target = "opcion.id")
    @Mapping(target = "opcionNombre", ignore = true)
    OpcionDetallePedido toEntity(OpcionDetallePedidoDTO dto);
}

