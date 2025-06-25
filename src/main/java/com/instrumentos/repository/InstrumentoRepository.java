package com.instrumentos.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.instrumentos.model.Categoria;
import com.instrumentos.model.Instrumento;

@Repository
public interface InstrumentoRepository extends JpaRepository<Instrumento, Long> {
    
    // Buscar por marca (case insensitive)
    List<Instrumento> findByMarcaContainingIgnoreCase(String marca);
    
    // Buscar por nombre de instrumento (case insensitive)
    List<Instrumento> findByInstrumentoContainingIgnoreCase(String instrumento);
    
    // Buscar por marca y modelo
    List<Instrumento> findByMarcaAndModelo(String marca, String modelo);
    
    // Buscar instrumentos con envío gratis
    List<Instrumento> findByCostoEnvio(String costoEnvio);

    // Buscar instrumentos por categoría
    List<Instrumento> findByCategoria(Categoria categoria);

    // Buscar instrumentos por ID de categoría
    List<Instrumento> findByCategoriaId(Long categoriaId);

    // Query personalizada para buscar por múltiples criterios incluyendo categoría
    @Query("SELECT i FROM Instrumento i WHERE " +
            "(:marca IS NULL OR LOWER(i.marca) LIKE LOWER(CONCAT('%', :marca, '%'))) AND " +
            "(:instrumento IS NULL OR LOWER(i.instrumento) LIKE LOWER(CONCAT('%', :instrumento, '%'))) AND " +
            "(:modelo IS NULL OR LOWER(i.modelo) LIKE LOWER(CONCAT('%', :modelo, '%'))) AND " +
            "(:categoriaId IS NULL OR i.categoria.id = :categoriaId)")
    List<Instrumento> findByMultipleCriteriaWithCategory(
            @Param("marca") String marca,
            @Param("instrumento") String instrumento,
            @Param("modelo") String modelo,
            @Param("categoriaId") Long categoriaId
    );
    
    // Query personalizada para buscar por múltiples criterios
    @Query("SELECT i FROM Instrumento i WHERE " +
           "(:marca IS NULL OR LOWER(i.marca) LIKE LOWER(CONCAT('%', :marca, '%'))) AND " +
           "(:instrumento IS NULL OR LOWER(i.instrumento) LIKE LOWER(CONCAT('%', :instrumento, '%'))) AND " +
           "(:modelo IS NULL OR LOWER(i.modelo) LIKE LOWER(CONCAT('%', :modelo, '%')))")
    List<Instrumento> findByMultipleCriteria(
            @Param("marca") String marca,
            @Param("instrumento") String instrumento,
            @Param("modelo") String modelo
    );
    
    // Paginación
    Page<Instrumento> findAll(Pageable pageable);
    
    // Contar instrumentos por marca
    @Query("SELECT i.marca, COUNT(i) FROM Instrumento i GROUP BY i.marca")
    List<Object[]> countByMarca();
}
