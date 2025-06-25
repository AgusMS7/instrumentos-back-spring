package com.instrumentos.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "instrumento")
public class Instrumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del instrumento es requerido")
    @Size(max = 255, message = "El nombre del instrumento no puede exceder 255 caracteres")
    @Column(name = "instrumento", nullable = false)
    private String instrumento;

    @NotBlank(message = "La marca es requerida")
    @Size(max = 100, message = "La marca no puede exceder 100 caracteres")
    @Column(name = "marca", nullable = false, length = 100)
    private String marca;

    @NotBlank(message = "El modelo es requerido")
    @Size(max = 100, message = "El modelo no puede exceder 100 caracteres")
    @Column(name = "modelo", nullable = false, length = 100)
    private String modelo;

    @Column(name = "imagen", length = 255)
    private String imagen;

    @NotNull(message = "El precio es requerido")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    @Column(name = "precio", precision = 10, scale = 2, nullable = false)
    private BigDecimal precio;

    @Size(max = 50, message = "El costo de envío no puede exceder 50 caracteres")
    @Column(name = "costo_envio", length = 50)
    private String costoEnvio = "0";

    @Min(value = 0, message = "La cantidad vendida no puede ser negativa")
    @Column(name = "cantidad_vendida")
    private Integer cantidadVendida = 0;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion = "";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_categoria")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Categoria categoria;

    // Constructores
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
                ", categoria=" + (categoria != null ? categoria.getDenominacion() : "null") +
                '}';
    }
}
