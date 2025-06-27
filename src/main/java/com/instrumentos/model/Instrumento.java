package com.instrumentos.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "instrumento")
public class Instrumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del instrumento es obligatorio")
    @Column(nullable = false)
    private String instrumento;

    @NotBlank(message = "La marca es obligatoria")
    @Column(nullable = false, length = 100)
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    @Column(nullable = false, length = 100)
    private String modelo;

    @Column(length = 255)
    private String imagen = "";

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser positivo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "costo_envio", length = 10)
    private String costoEnvio = "0";

    @Column(name = "cantidadvendida")
    private Integer cantidadVendida = 0;

    @Column(name = "cantidad_vendida")
    private Integer cantidadVendidaAlt = 0;

    @Column(columnDefinition = "TEXT")
    private String descripcion = "";

    @Column(name = "id_categoria")
    private Long idCategoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", insertable = false, updatable = false)
    private Categoria categoria;

    @OneToMany(mappedBy = "instrumento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductImage> imagenes = new ArrayList<>();

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

    public Integer getCantidadVendidaAlt() {
        return cantidadVendidaAlt;
    }

    public void setCantidadVendidaAlt(Integer cantidadVendidaAlt) {
        this.cantidadVendidaAlt = cantidadVendidaAlt;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<ProductImage> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<ProductImage> imagenes) {
        this.imagenes = imagenes;
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
