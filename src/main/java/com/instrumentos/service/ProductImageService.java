package com.instrumentos.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.instrumentos.dto.ProductImageDTO;
import com.instrumentos.model.Instrumento;
import com.instrumentos.model.ProductImage;
import com.instrumentos.repository.InstrumentoRepository;
import com.instrumentos.repository.ProductImageRepository;

@Service
@Transactional
public class ProductImageService {
    
    @Autowired
    private ProductImageRepository productImageRepository;
    
    @Autowired
    private InstrumentoRepository instrumentoRepository;
    
    private static final String UPLOAD_DIR = "public/images/";
    
    public ProductImageDTO createImage(Long instrumentoId, String filename, String altText, boolean isPrimary) {
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
        return convertToDTO(savedImage);
    }
    
    public List<ProductImageDTO> getImagesByInstrumento(Long instrumentoId) {
        List<ProductImage> images = productImageRepository.findByInstrumentoIdOrderByIsPrimaryDescIdAsc(instrumentoId);
        return images.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public ProductImageDTO getPrimaryImage(Long instrumentoId) {
        ProductImage primaryImage = productImageRepository.findByInstrumentoIdAndIsPrimaryTrue(instrumentoId);
        return primaryImage != null ? convertToDTO(primaryImage) : null;
    }
    
    public ProductImageDTO setPrimaryImage(Long imageId) {
        ProductImage image = productImageRepository.findById(imageId)
            .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
        
        // Desmarcar otras imágenes principales del mismo instrumento
        productImageRepository.clearPrimaryImages(image.getInstrumento().getId());
        
        // Marcar esta como principal
        image.setIsPrimary(true);
        ProductImage savedImage = productImageRepository.save(image);
        
        return convertToDTO(savedImage);
    }
    
    public void deleteImage(Long imageId) {
        ProductImage image = productImageRepository.findById(imageId)
            .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
        
        // Eliminar archivo físico
        try {
            Path imagePath = Paths.get(UPLOAD_DIR).resolve(image.getImageUrl());
            Files.deleteIfExists(imagePath);
        } catch (IOException e) {
            System.err.println("Error al eliminar archivo físico: " + e.getMessage());
        }
        
        // Eliminar registro de base de datos
        productImageRepository.delete(image);
    }

    public void deleteImagesByInstrumento(Long instrumentoId) {
        List<ProductImage> images = productImageRepository.findByInstrumentoId(instrumentoId);
        
        // Eliminar archivos físicos
        images.forEach(image -> {
            try {
                Path imagePath = Paths.get(UPLOAD_DIR).resolve(image.getImageUrl());
                Files.deleteIfExists(imagePath);
            } catch (IOException e) {
                System.err.println("Error al eliminar archivo físico: " + e.getMessage());
            }
        });
        
        // Eliminar registros de base de datos
        productImageRepository.deleteByInstrumentoId(instrumentoId);
    }
    
    private ProductImageDTO convertToDTO(ProductImage image) {
        ProductImageDTO dto = new ProductImageDTO();
        dto.setId(image.getId());
        dto.setInstrumentoId(image.getInstrumento().getId());
        dto.setImageUrl(image.getImageUrl());
        dto.setAltText(image.getAltText());
        dto.setIsPrimary(image.getIsPrimary());
        return dto;
    }
}
