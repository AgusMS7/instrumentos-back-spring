package com.instrumentos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.instrumentos.model.ProductImage;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    
    // Encontrar todas las imágenes de un instrumento
    List<ProductImage> findByInstrumentoId(Long instrumentoId);
    
    // Encontrar la imagen principal de un instrumento
    Optional<ProductImage> findByInstrumentoIdAndIsPrimaryTrue(Long instrumentoId);
    
    // Encontrar todas las imágenes principales
    List<ProductImage> findByIsPrimaryTrue();
    
    // Contar imágenes por instrumento
    long countByInstrumentoId(Long instrumentoId);
    
    // Query personalizada para obtener imágenes ordenadas (primaria primero)
    @Query("SELECT pi FROM ProductImage pi WHERE pi.instrumento.id = :instrumentoId ORDER BY pi.isPrimary DESC, pi.createdAt ASC")
    List<ProductImage> findByInstrumentoIdOrderByPrimaryFirst(@Param("instrumentoId") Long instrumentoId);
    
    // Eliminar todas las imágenes de un instrumento
    void deleteByInstrumentoId(Long instrumentoId);
}
