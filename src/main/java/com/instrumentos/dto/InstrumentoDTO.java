package com.instrumentos.dto;

import java.math.BigDecimal;

import com.instrumentos.model.Instrumento;

public class InstrumentoDTO {
    private Long id;
    private String instrumento;
    private String marca;
    private String modelo;
    private String imagen;
    private BigDecimal precio; // Cambiar a BigDecimal
    private String costoEnvio;
    private Integer cantidadVendida; // Cambiar a Integer
    private String descripcion;
    private Long idCategoria;
    private String nombreCategoria;
    
    // Constructores
    public InstrumentoDTO() {}
    
    public InstrumentoDTO(Long id, String instrumento, String marca, String modelo, 
                         String imagen, BigDecimal precio, String costoEnvio, 
                         Integer cantidadVendida, String descripcion, Long idCategoria) {
        this.id = id;
        this.instrumento = instrumento;
        this.marca = marca;
        this.modelo = modelo;
        this.imagen = imagen;
        this.precio = precio;
        this.costoEnvio = costoEnvio;
        this.cantidadVendida = cantidadVendida;
        this.descripcion = descripcion;
        this.idCategoria = idCategoria;
    }
    
    // Método estático para convertir Entity a DTO
    public static InstrumentoDTO fromEntity(Instrumento instrumento) {
        InstrumentoDTO dto = new InstrumentoDTO();
        dto.setId(instrumento.getId());
        dto.setInstrumento(instrumento.getInstrumento());
        dto.setMarca(instrumento.getMarca());
        dto.setModelo(instrumento.getModelo());
        dto.setImagen(instrumento.getImagen());
        dto.setPrecio(instrumento.getPrecio());
        dto.setCostoEnvio(instrumento.getCostoEnvio());
        dto.setCantidadVendida(instrumento.getCantidadVendida());
        dto.setDescripcion(instrumento.getDescripcion());
        
        if (instrumento.getCategoria() != null) {
            dto.setIdCategoria(instrumento.getCategoria().getId());
            dto.setNombreCategoria(instrumento.getCategoria().getDenominacion());
        }
        
        return dto;
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

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }
}
