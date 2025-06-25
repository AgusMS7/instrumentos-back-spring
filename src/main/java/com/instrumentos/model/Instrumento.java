package com.instrumentos.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "instrumento")
public class Instrumento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
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
    
    @NotNull(message = "El precio es requerido")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio; // Cambiar a BigDecimal para manejar DECIMAL/NUMERIC
    
    @Column(name = "costoenvio", length = 10)
    private String costoEnvio = "0";
    
    @Min(value = 0, message = "La cantidad vendida no puede ser negativa")
    @Column(name = "cantidadvendida")
    private Integer cantidadVendida = 0;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion = "";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idcategoria")
    private Categoria categoria;
    
    // Constructores
    public Instrumento() {}
    
    public Instrumento(String instrumento, String marca, String modelo, BigDecimal precio) {
        this.instrumento = instrumento;
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
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
    
    public BigDecimal getPrecio() {
        return precio;
    }
    
    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
    
    public String getCostoEnvio() {
        return costoEnvio;
    }
    
    public void setCostoEnvio(String costoEnvio) {
        this.costoEnvio = costoEnvio;
    }
    
    public Integer getCantidadVendida() {
        return cantidadVendida;
    }
    
    public void setCantidadVendida(Integer cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    
    // Getter para idCategoria (para compatibilidad con frontend)
    public Long getIdCategoria() {
        return categoria != null ? categoria.getId() : null;
    }
    
    // Setter para idCategoria (para compatibilidad con frontend)
    public void setIdCategoria(Long idCategoria) {
        if (idCategoria != null) {
            Categoria cat = new Categoria();
            cat.setId(idCategoria);
            this.categoria = cat;
        } else {
            this.categoria = null;
        }
    }
    
    @Override
    public String toString() {
        return "Instrumento{" +
                "id=" + id +
                ", instrumento='" + instrumento + '\'' +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", precio=" + precio +
                '}';
    }
}
