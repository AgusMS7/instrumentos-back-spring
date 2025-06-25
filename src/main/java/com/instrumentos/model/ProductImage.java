package com.instrumentos.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "product_images")
public class ProductImage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrumento_id", nullable = false)
    @NotNull(message = "El instrumento es requerido")
    private Instrumento instrumento;
    
    @NotBlank(message = "La URL de la imagen es requerida")
    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;
    
    @Column(name = "alt_text", length = 255)
    private String altText;
    
    @Column(name = "is_primary")
    private Boolean isPrimary = false;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Constructores
    public ProductImage() {}
    
    public ProductImage(Instrumento instrumento, String imageUrl, String altText, Boolean isPrimary) {
        this.instrumento = instrumento;
        this.imageUrl = imageUrl;
        this.altText = altText;
        this.isPrimary = isPrimary;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Instrumento getInstrumento() {
        return instrumento;
    }
    
    public void setInstrumento(Instrumento instrumento) {
        this.instrumento = instrumento;
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
    
    @Override
    public String toString() {
        return "ProductImage{" +
                "id=" + id +
                ", imageUrl='" + imageUrl + '\'' +
                ", altText='" + altText + '\'' +
                ", isPrimary=" + isPrimary +
                '}';
    }
}
