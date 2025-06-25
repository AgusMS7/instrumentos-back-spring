package com.instrumentos.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.instrumentos.model.Instrumento;

@Repository
public interface InstrumentoRepository extends JpaRepository<Instrumento, Long> {
    
    // Buscar por categoría
    @Query("SELECT i FROM Instrumento i JOIN FETCH i.categoria WHERE i.categoria.id = :categoriaId")
    List<Instrumento> findByCategoriaId(@Param("categoriaId") Long categoriaId);
    
    // Buscar por nombre (contiene, ignorando mayúsculas)
    @Query("SELECT i FROM Instrumento i JOIN FETCH i.categoria WHERE LOWER(i.instrumento) LIKE LOWER(CONCAT('%', :instrumento, '%'))")
    List<Instrumento> findByInstrumentoContainingIgnoreCase(@Param("instrumento") String instrumento);
    
    // Buscar por marca
    @Query("SELECT i FROM Instrumento i JOIN FETCH i.categoria WHERE LOWER(i.marca) LIKE LOWER(CONCAT('%', :marca, '%'))")
    List<Instrumento> findByMarcaContainingIgnoreCase(@Param("marca") String marca);
    
    // Buscar instrumentos con precio menor o igual
    @Query("SELECT i FROM Instrumento i JOIN FETCH i.categoria WHERE i.precio <= :precio")
    List<Instrumento> findByPrecioLessThanEqual(@Param("precio") BigDecimal precio);
    
    // Query personalizada para buscar por múltiples campos
    @Query("SELECT i FROM Instrumento i JOIN FETCH i.categoria WHERE " +
           "LOWER(i.instrumento) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(i.marca) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(i.modelo) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Instrumento> findByMultipleFields(@Param("searchTerm") String searchTerm);
    
    // Obtener instrumentos más vendidos
    @Query("SELECT i FROM Instrumento i JOIN FETCH i.categoria ORDER BY i.cantidadVendida DESC")
    List<Instrumento> findTopSellingInstruments();
    
    // Override del findAll para incluir categoría
    @Query("SELECT i FROM Instrumento i JOIN FETCH i.categoria")
    @Override
    List<Instrumento> findAll();
}
