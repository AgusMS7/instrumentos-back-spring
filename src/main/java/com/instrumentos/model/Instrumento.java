package com.instrumentos.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "instrumento",
       indexes = {
           @Index(name = "idx_instrumento_categoria", columnList = "id_categoria"),
           @Index(name = "idx_instrumento_precio", columnList = "precio"),
           @Index(name = "idx_instrumento_nombre", columnList = "instrumento")
       })
public class Instrumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del instrumento es requerido")
    @Size(min = 2, max = 255, message = "El nombre del instrumento debe tener entre 2 y 255 caracteres")
    @Column(name = "instrumento", nullable = false)
    private String instrumento;

    @NotBlank(message = "La marca es requerida")
    @Size(min = 1, max = 100, message = "La marca debe tener entre 1 y 100 caracteres")
    @Column(name = "marca", nullable = false, length = 100)
    private String marca;

    @NotBlank(message = "El modelo es requerido")
    @Size(min = 1, max = 100, message = "El modelo debe tener entre 1 y 100 caracteres")
    @Column(name = "modelo", nullable = false, length = 100)
    private String modelo;

    @Size(max = 255, message = "El nombre de la imagen no puede exceder 255 caracteres")
    @Column(name = "imagen", length = 255)
    private String imagen;

    @NotNull(message = "El precio es requerido")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "El precio debe tener máximo 8 dígitos enteros y 2 decimales")
    @Column(name = "precio", precision = 10, scale = 2, nullable = false)
    private BigDecimal precio;

    @Pattern(regexp = "^[GP]$", message = "El costo de envío debe ser 'G' (Gratis) o 'P' (Pago)")
    @Column(name = "costo_envio", length = 1)
    private String costoEnvio = "G";

    @Min(value = 0, message = "La cantidad vendida no puede ser negativa")
    @Column(name = "cantidad_vendida")
    private Integer cantidadVendida = 0;

    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_categoria", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @NotNull(message = "La categoría es requerida")
    private Categoria categoria;

    public Instrumento() {}

    public Instrumento(String instrumento, String marca, String modelo, String imagen, 
                      BigDecimal precio, String costoEnvio, Integer cantidadVendida, 
                      String descripcion, Categoria categoria) {
        this.instrumento = instrumento;
        this.marca = marca;
        this.modelo = modelo;
        this.imagen = imagen;
        this.precio = precio;
        this.costoEnvio = costoEnvio;
        this.cantidadVendida = cantidadVendida;
        this.descripcion = descripcion;
        this.categoria = categoria;
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
        this.instrumento = instrumento != null ? instrumento.trim() : null;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca != null ? marca.trim() : null;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo != null ? modelo.trim() : null;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen != null ? imagen.trim() : null;
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
        this.descripcion = descripcion != null ? descripcion.trim() : null;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    // Getter para idCategoria (compatibilidad con frontend)
    public Long getIdCategoria() {
        return categoria != null ? categoria.getId() : null;
    }

    // Setter para idCategoria (compatibilidad con frontend)
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
                ", categoria=" + (categoria != null ? categoria.getDenominacion() : "null") +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Instrumento)) return false;
        Instrumento that = (Instrumento) o;
        return id != null && id.equals(that.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
