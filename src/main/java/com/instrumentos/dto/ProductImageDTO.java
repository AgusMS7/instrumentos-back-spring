package com.instrumentos.dto;

import com.instrumentos.model.ProductImage;

public class ProductImageDTO {
    private Integer id;
    private String filename;
    private String altText;
    private String imageType;
    private Integer displayOrder;
    private String url;
    
    // Constructores
    public ProductImageDTO() {}
    
    // Método estático para convertir Entity a DTO
    public static ProductImageDTO fromEntity(ProductImage image, String baseUrl) {
        ProductImageDTO dto = new ProductImageDTO();
        dto.setId(image.getId());
        dto.setFilename(image.getFilename());
        dto.setAltText(image.getAltText());
        dto.setImageType(image.getImageType());
        dto.setDisplayOrder(image.getDisplayOrder());
        dto.setUrl(baseUrl + "/images/" + image.getFilename());
        return dto;
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getFilename() {
        return filename;
    }
    
    public void setFilename(String filename) {
        this.filename = filename;
    }
    
    public String getAltText() {
        return altText;
    }
    
    public void setAltText(String altText) {
        this.altText = altText;
    }
    
    public String getImageType() {
        return imageType;
    }
    
    public void setImageType(String imageType) {
        this.imageType = imageType;
    }
    
    public Integer getDisplayOrder() {
        return displayOrder;
    }
    
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
}
