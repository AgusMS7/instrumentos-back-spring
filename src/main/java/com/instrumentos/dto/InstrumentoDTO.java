package com.instrumentos.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.instrumentos.model.Instrumento;
import com.instrumentos.model.ProductImage;

public class InstrumentoDTO {
    private Integer id;
    private String instrumento;
    private String marca;
    private String modelo;
    private String precio;
    private String costoenvio;
    private String cantidadvendida;
    private String descripcion;
    private String imagen; // Para compatibilidad
    private List<ProductImageDTO> images;
    private ProductImageDTO mainImage;
    
    // Constructores
    public InstrumentoDTO() {}
    
    // Método estático para convertir Entity a DTO
    public static InstrumentoDTO fromEntity(Instrumento instrumento, String baseUrl) {
        InstrumentoDTO dto = new InstrumentoDTO();
        dto.setId(instrumento.getId());
        dto.setInstrumento(instrumento.getInstrumento());
        dto.setMarca(instrumento.getMarca());
        dto.setModelo(instrumento.getModelo());
        dto.setPrecio(instrumento.getPrecio());
        dto.setCostoenvio(instrumento.getCostoEnvio());
        dto.setCantidadvendida(instrumento.getCantidadVendida());
        dto.setDescripcion(instrumento.getDescripcion());
        
        // Convertir imágenes
        if (instrumento.getImages() != null && !instrumento.getImages().isEmpty()) {
            List<ProductImageDTO> imageDTOs = instrumento.getImages().stream()
                    .map(img -> ProductImageDTO.fromEntity(img, baseUrl))
                    .collect(Collectors.toList());
            dto.setImages(imageDTOs);
            
            // Establecer imagen principal
            ProductImage mainImg = instrumento.getMainImage();
            if (mainImg != null) {
                dto.setMainImage(ProductImageDTO.fromEntity(mainImg, baseUrl));
                dto.setImagen(mainImg.getFilename()); // Compatibilidad
            }
        }
        
        return dto;
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getInstrumento() {
        return instrumento;
    }
    
    public void setInstrumento(String instrumento) {
        this.instrumento = instrumento;
    }
    
    public String getMarca() {
        return marca;
    }
    
    public void setMarca(String marca) {
        this.marca = marca;
    }
    
    public String getModelo() {
        return modelo;
    }
    
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    
    public String getPrecio() {
        return precio;
    }
    
    public void setPrecio(String precio) {
        this.precio = precio;
    }
    
    public String getCostoenvio() {
        return costoenvio;
    }
    
    public void setCostoenvio(String costoenvio) {
        this.costoenvio = costoenvio;
    }
    
    public String getCantidadvendida() {
        return cantidadvendida;
    }
    
    public void setCantidadvendida(String cantidadvendida) {
        this.cantidadvendida = cantidadvendida;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getImagen() {
        return imagen;
    }
    
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    
    public List<ProductImageDTO> getImages() {
        return images;
    }
    
    public void setImages(List<ProductImageDTO> images) {
        this.images = images;
    }
    
    public ProductImageDTO getMainImage() {
        return mainImage;
    }
    
    public void setMainImage(ProductImageDTO mainImage) {
        this.mainImage = mainImage;
    }
}
