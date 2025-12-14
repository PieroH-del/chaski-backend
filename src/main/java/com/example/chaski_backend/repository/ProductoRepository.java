package com.example.chaski_backend.repository;

import com.example.chaski_backend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByRestauranteId(Long restauranteId);
    List<Producto> findByRestauranteIdAndDisponible(Long restauranteId, Boolean disponible);
}

