package com.example.chaski_backend.repository;

import com.example.chaski_backend.enums.EstadoPedido;
import com.example.chaski_backend.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuarioId(Long usuarioId);
    List<Pedido> findByUsuarioIdOrderByFechaCreacionDesc(Long usuarioId);
    List<Pedido> findByEstado(EstadoPedido estado);
    List<Pedido> findByRestauranteId(Long restauranteId);
}

