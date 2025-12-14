package com.example.chaski_backend.mapper;

import com.example.chaski_backend.dto.DetallePedidoDTO;
import com.example.chaski_backend.model.DetallePedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OpcionDetallePedidoMapper.class})
public interface DetallePedidoMapper {

    @Mapping(source = "pedido.id", target = "pedidoId")
    @Mapping(source = "producto.id", target = "productoId")
    @Mapping(source = "producto.nombre", target = "productoNombre")
    DetallePedidoDTO toDto(DetallePedido entity);

    @Mapping(source = "pedidoId", target = "pedido.id")
    @Mapping(source = "productoId", target = "producto.id")
    @Mapping(target = "productoNombre", ignore = true)
    DetallePedido toEntity(DetallePedidoDTO dto);
}

