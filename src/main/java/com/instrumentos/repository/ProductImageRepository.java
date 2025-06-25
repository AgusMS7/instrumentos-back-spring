package com.instrumentos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.instrumentos.model.ProductImage;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    
    // Obtener imágenes por instrumento ID
    List<ProductImage> findByInstrumentoIdOrderByDisplayOrderAsc(Integer instrumentoId);
    
    // Obtener imagen principal de un instrumento
    Optional<ProductImage> findByInstrumentoIdAndImageType(Integer instrumentoId, String imageType);
    
    // Obtener todas las imágenes de un tipo específico
    List<ProductImage> findByImageTypeOrderByDisplayOrderAsc(String imageType);
    
    // Query para obtener imágenes con información del instrumento
    @Query("SELECT pi FROM ProductImage pi JOIN pi.instrumento i WHERE i.id = :instrumentoId ORDER BY pi.imageType DESC, pi.displayOrder ASC")
    List<ProductImage> findImagesByInstrumentoId(@Param("instrumentoId") Integer instrumentoId);
}
