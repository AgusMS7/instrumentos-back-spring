package com.instrumentos.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.instrumentos.dto.ProductImageDTO;
import com.instrumentos.model.Instrumento;
import com.instrumentos.model.ProductImage;
import com.instrumentos.repository.InstrumentoRepository;
import com.instrumentos.repository.ProductImageRepository;

@Service
@Transactional(readOnly = true)
public class ProductImageService {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductImageService.class);
    private static final String UPLOAD_DIR = "public/images/";
    
    @Autowired
    private ProductImageRepository productImageRepository;
    
    @Autowired
    private InstrumentoRepository instrumentoRepository;
    
    @Transactional
    public ProductImageDTO createImage(Long instrumentoId, String filename, String altText, boolean isPrimary) {
        logger.debug("Creando imagen para instrumento ID: {}, archivo: {}", instrumentoId, filename);
        
        Instrumento instrumento = instrumentoRepository.findById(instrumentoId)
            .orElseThrow(() -> new RuntimeException("Instrumento no encontrado"));
        
        // Si esta imagen será la principal, desmarcar las demás
        if (isPrimary) {
            productImageRepository.clearPrimaryImages(instrumentoId);
        }
        
        ProductImage image = new ProductImage();
        image.setInstrumento(instrumento);
        image.setImageUrl(filename);
        image.setAltText(altText);
        image.setIsPrimary(isPrimary);
        
        ProductImage savedImage = productImageRepository.save(image);
        logger.info("Imagen creada exitosamente con ID: {}", savedImage.getId());
        
        return convertToDTO(savedImage);
    }
    
    public List<ProductImageDTO> getImagesByInstrumento(Long instrumentoId) {
        logger.debug("Obteniendo imágenes del instrumento ID: {}", instrumentoId);
        
        List<ProductImage> images = productImageRepository.findByInstrumentoIdOrderByIsPrimaryDescIdAsc(instrumentoId);
        return images.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public ProductImageDTO getPrimaryImage(Long instrumentoId) {
        logger.debug("Obteniendo imagen principal del instrumento ID: {}", instrumentoId);
        
        ProductImage primaryImage = productImageRepository.findByInstrumentoIdAndIsPrimaryTrue(instrumentoId);
        return primaryImage != null ? convertToDTO(primaryImage) : null;
    }
    
    @Transactional
    public ProductImageDTO setPrimaryImage(Long imageId) {
        logger.debug("Estableciendo imagen ID {} como principal", imageId);
        
        ProductImage image = productImageRepository.findById(imageId)
            .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
        
        // Desmarcar otras imágenes principales del mismo instrumento
        productImageRepository.clearPrimaryImages(image.getInstrumento().getId());
        
        // Marcar esta como principal
        image.setIsPrimary(true);
        ProductImage savedImage = productImageRepository.save(image);
        
        logger.info("Imagen ID {} establecida como principal", imageId);
        return convertToDTO(savedImage);
    }
    
    @Transactional
    public void deleteImage(Long imageId) {
        logger.debug("Eliminando imagen ID: {}", imageId);
        
        ProductImage image = productImageRepository.findById(imageId)
            .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
        
        // Eliminar archivo físico
        deletePhysicalFile(image.getImageUrl());
        
        // Eliminar registro de base de datos
        productImageRepository.delete(image);
        
        logger.info("Imagen eliminada exitosamente: {}", imageId);
    }

    @Transactional
    public void deleteImagesByInstrumento(Long instrumentoId) {
        logger.debug("Eliminando todas las imágenes del instrumento ID: {}", instrumentoId);
        
        List<ProductImage> images = productImageRepository.findByInstrumentoId(instrumentoId);
        
        // Eliminar archivos físicos
        images.forEach(image -> deletePhysicalFile(image.getImageUrl()));
        
        // Eliminar registros de base de datos
        productImageRepository.deleteByInstrumentoId(instrumentoId);
        
        logger.info("Eliminadas {} imágenes del instrumento ID: {}", images.size(), instrumentoId);
    }
    
    private void deletePhysicalFile(String filename) {
        try {
            Path imagePath = Paths.get(UPLOAD_DIR).resolve(filename);
            boolean deleted = Files.deleteIfExists(imagePath);
            if (deleted) {
                logger.debug("Archivo físico eliminado: {}", filename);
            } else {
                logger.warn("Archivo físico no encontrado: {}", filename);
            }
        } catch (IOException e) {
            logger.error("Error al eliminar archivo físico {}: {}", filename, e.getMessage());
        }
    }
    
    private ProductImageDTO convertToDTO(ProductImage image) {
        return ProductImageDTO.fromEntity(image);
    }
}
