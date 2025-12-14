package com.example.chaski_backend.mapper;

import com.example.chaski_backend.dto.PedidoDTO;
import com.example.chaski_backend.model.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DetallePedidoMapper.class})
public interface PedidoMapper {

    @Mapping(source = "usuario.id", target = "usuarioId")
    @Mapping(source = "restaurante.id", target = "restauranteId")
    @Mapping(source = "direccionEntrega.id", target = "direccionEntregaId")
    PedidoDTO toDto(Pedido entity);


    @Mapping(source = "usuarioId", target = "usuario.id")
    @Mapping(source = "restauranteId", target = "restaurante.id")
    @Mapping(source = "direccionEntregaId", target = "direccionEntrega.id")
    Pedido toEntity(PedidoDTO dto);
}

