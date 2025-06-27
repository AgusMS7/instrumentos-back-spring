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
    List<Instrumento> findByIdCategoria(Long categoriaId);

    // Buscar por nombre (case insensitive)
    List<Instrumento> findByInstrumentoContainingIgnoreCase(String nombre);

    // Buscar por marca
    List<Instrumento> findByMarcaContainingIgnoreCase(String marca);

    // Buscar por rango de precios
    List<Instrumento> findByPrecioBetween(BigDecimal precioMin, BigDecimal precioMax);

    // Buscar por precio menor o igual
    List<Instrumento> findByPrecioLessThanEqual(BigDecimal precio);

    // Buscar por precio mayor o igual
    List<Instrumento> findByPrecioGreaterThanEqual(BigDecimal precio);

    // Buscar instrumentos con envío gratis
    @Query("SELECT i FROM Instrumento i WHERE i.costoEnvio = 'G'")
    List<Instrumento> findInstrumentosConEnvioGratis();

    // Buscar instrumentos más vendidos
    @Query("SELECT i FROM Instrumento i ORDER BY i.cantidadVendida DESC")
    List<Instrumento> findInstrumentosMasVendidos();

    // Buscar instrumentos por categoría con información de categoría
    @Query("SELECT i FROM Instrumento i LEFT JOIN FETCH i.categoria WHERE i.idCategoria = :categoriaId")
    List<Instrumento> findByIdCategoriaWithCategoria(@Param("categoriaId") Long categoriaId);

    // Buscar instrumentos con imágenes
    @Query("SELECT DISTINCT i FROM Instrumento i LEFT JOIN FETCH i.imagenes")
    List<Instrumento> findAllWithImages();

    // Buscar por ID con imágenes
    @Query("SELECT i FROM Instrumento i LEFT JOIN FETCH i.imagenes WHERE i.id = :id")
    Instrumento findByIdWithImages(@Param("id") Long id);

    // Contar instrumentos por categoría
    long countByIdCategoria(Long categoriaId);

    // Verificar si existe por nombre exacto
    boolean existsByInstrumentoIgnoreCase(String nombre);
}
