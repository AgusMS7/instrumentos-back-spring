package com.instrumentos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.instrumentos.model.ProductImage;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    @Modifying
    @Query("UPDATE ProductImage p SET p.isPrimary = false WHERE p.instrumento.id = :instrumentoId")
    void clearPrimaryImages(@Param("instrumentoId") Long instrumentoId);

    List<ProductImage> findByInstrumentoIdOrderByIsPrimaryDescIdAsc(Long instrumentoId);

    ProductImage findByInstrumentoIdAndIsPrimaryTrue(Long instrumentoId);

    List<ProductImage> findByInstrumentoId(Long instrumentoId);

    void deleteByInstrumentoId(Long instrumentoId);
}
