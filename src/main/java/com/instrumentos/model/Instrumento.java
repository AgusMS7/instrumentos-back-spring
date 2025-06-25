package com.instrumentos.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "instrumento")
public class Instrumento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank(message = "El nombre del instrumento es requerido")
    @Column(nullable = false)
    private String instrumento;
    
    @NotBlank(message = "La marca es requerida")
    @Column(nullable = false, length = 100)
    private String marca;
    
    @NotBlank(message = "El modelo es requerido")
    @Column(nullable = false, length = 100)
    private String modelo;
    
    @Column(length = 255)
    private String imagen;
    
    @NotBlank(message = "El precio es requerido")
    @Column(nullable = false, length = 20)
    private String precio;
    
    @Column(name = "costoenvio", length = 10)
    private String costoEnvio = "0";
    
    @Column(name = "cantidadvendida", length = 10)
    private String cantidadVendida = "0";
    
    @Column(columnDefinition = "TEXT")
    private String descripcion = "";
    
    @OneToMany(mappedBy = "instrumento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ProductImage> images = new ArrayList<>();
    
    // Constructores
    public Instrumento() {}
    
    public Instrumento(String instrumento, String marca, String modelo, String precio) {
        this.instrumento = instrumento;
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
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
    
    public String getImagen() {
        return imagen;
    }
    
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    
    public String getPrecio() {
        return precio;
    }
    
    public void setPrecio(String precio) {
        this.precio = precio;
    }
    
    public String getCostoEnvio() {
        return costoEnvio;
    }
    
    public void setCostoEnvio(String costoEnvio) {
        this.costoEnvio = costoEnvio;
    }
    
    public String getCantidadVendida() {
        return cantidadVendida;
    }
    
    public void setCantidadVendida(String cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public List<ProductImage> getImages() {
        return images;
    }
    
    public void setImages(List<ProductImage> images) {
        this.images = images;
    }
    
    // Método helper para obtener imagen principal
    public ProductImage getMainImage() {
        return images.stream()
                .filter(img -> "main".equals(img.getImageType()))
                .findFirst()
                .orElse(images.isEmpty() ? null : images.get(0));
    }
    
    @Override
    public String toString() {
        return "Instrumento{" +
                "id=" + id +
                ", instrumento='" + instrumento + '\'' +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", precio='" + precio + '\'' +
                '}';
    }
}
