package com.example.chaski_backend.repository;

import com.example.chaski_backend.enums.EstadoPago;
import com.example.chaski_backend.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    Optional<Pago> findByPedidoId(Long pedidoId);
    Optional<Pago> findByReferenciaPasarela(String referenciaPasarela);
}

