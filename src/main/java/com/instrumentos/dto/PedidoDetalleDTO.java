package com.instrumentos.dto;

import com.instrumentos.model.PedidoDetalle;

import java.math.BigDecimal;

public class PedidoDetalleDTO {
    
    private Long id;
    private Integer cantidad;
    private Long instrumentoId;
    private String instrumentoNombre;
    private String instrumentoMarca;
    private String instrumentoModelo;
    private BigDecimal instrumentoPrecio;

    // Constructores
    public PedidoDetalleDTO() {}

    public PedidoDetalleDTO(Long id, Integer cantidad, Long instrumentoId, 
                           String instrumentoNombre, String instrumentoMarca, 
                           String instrumentoModelo, BigDecimal instrumentoPrecio) {
        this.id = id;
        this.cantidad = cantidad;
        this.instrumentoId = instrumentoId;
        this.instrumentoNombre = instrumentoNombre;
        this.instrumentoMarca = instrumentoMarca;
        this.instrumentoModelo = instrumentoModelo;
        this.instrumentoPrecio = instrumentoPrecio;
    }

    // Método estático para convertir desde entidad
    public static PedidoDetalleDTO fromEntity(PedidoDetalle detalle) {
        if (detalle == null) {
            return null;
        }
        return new PedidoDetalleDTO(
            detalle.getId(),
            detalle.getCantidad(),
            detalle.getInstrumento().getId(),
            detalle.getInstrumento().getInstrumento(),
            detalle.getInstrumento().getMarca(),
            detalle.getInstrumento().getModelo(),
            detalle.getInstrumento().getPrecio()
        );
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Long getInstrumentoId() {
        return instrumentoId;
    }

    public void setInstrumentoId(Long instrumentoId) {
        this.instrumentoId = instrumentoId;
    }

    public String getInstrumentoNombre() {
        return instrumentoNombre;
    }

    public void setInstrumentoNombre(String instrumentoNombre) {
        this.instrumentoNombre = instrumentoNombre;
    }

    public String getInstrumentoMarca() {
        return instrumentoMarca;
    }

    public void setInstrumentoMarca(String instrumentoMarca) {
        this.instrumentoMarca = instrumentoMarca;
    }

    public String getInstrumentoModelo() {
        return instrumentoModelo;
    }

    public void setInstrumentoModelo(String instrumentoModelo) {
        this.instrumentoModelo = instrumentoModelo;
    }

    public BigDecimal getInstrumentoPrecio() {
        return instrumentoPrecio;
    }

    public void setInstrumentoPrecio(BigDecimal instrumentoPrecio) {
        this.instrumentoPrecio = instrumentoPrecio;
    }

    @Override
    public String toString() {
        return "PedidoDetalleDTO{" +
                "id=" + id +
                ", cantidad=" + cantidad +
                ", instrumentoId=" + instrumentoId +
                ", instrumentoNombre='" + instrumentoNombre + '\'' +
                '}';
    }
}
