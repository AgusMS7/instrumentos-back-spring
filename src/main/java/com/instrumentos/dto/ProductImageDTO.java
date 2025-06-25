package com.instrumentos.dto;

import java.time.LocalDateTime;

import com.instrumentos.model.ProductImage;

public class ProductImageDTO {
    private Long id;
    private Long instrumentoId;
    private String imageUrl;
    private String altText;
    private Boolean isPrimary;
    private LocalDateTime createdAt;
    
    // Constructores
    public ProductImageDTO() {}
    
    public ProductImageDTO(Long id, Long instrumentoId, String imageUrl, String altText, Boolean isPrimary, LocalDateTime createdAt) {
        this.id = id;
        this.instrumentoId = instrumentoId;
        this.imageUrl = imageUrl;
        this.altText = altText;
        this.isPrimary = isPrimary;
        this.createdAt = createdAt;
    }
    
    // Método estático para convertir Entity a DTO
    public static ProductImageDTO fromEntity(ProductImage productImage) {
        ProductImageDTO dto = new ProductImageDTO();
        dto.setId(productImage.getId());
        dto.setInstrumentoId(productImage.getInstrumento().getId());
        dto.setImageUrl(productImage.getImageUrl());
        dto.setAltText(productImage.getAltText());
        dto.setIsPrimary(productImage.getIsPrimary());
        dto.setCreatedAt(productImage.getCreatedAt());
        return dto;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getInstrumentoId() {
        return instrumentoId;
    }
    
    public void setInstrumentoId(Long instrumentoId) {
        this.instrumentoId = instrumentoId;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public String getAltText() {
        return altText;
    }
    
    public void setAltText(String altText) {
        this.altText = altText;
    }
    
    public Boolean getIsPrimary() {
        return isPrimary;
    }
    
    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
