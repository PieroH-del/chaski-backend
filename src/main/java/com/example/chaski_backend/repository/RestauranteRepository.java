package com.example.chaski_backend.repository;

import com.example.chaski_backend.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    List<Restaurante> findByEstaAbierto(Boolean estaAbierto);

    @Query("SELECT r FROM Restaurante r JOIN r.categorias c WHERE c.id = :categoriaId")
    List<Restaurante> findByCategoriaId(Long categoriaId);

    List<Restaurante> findByNombreContainingIgnoreCase(String nombre);
}

