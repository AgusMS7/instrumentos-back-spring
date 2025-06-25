package com.instrumentos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.instrumentos.model.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    // Buscar por denominación (case insensitive)
    List<Categoria> findByDenominacionContainingIgnoreCase(String denominacion);
    
    // Obtener todas las categorías ordenadas por denominación
    @Query("SELECT c FROM Categoria c ORDER BY c.denominacion ASC")
    List<Categoria> findAllOrderByDenominacion();
    
    // Verificar si existe una categoría con esa denominación
    boolean existsByDenominacionIgnoreCase(String denominacion);
}
