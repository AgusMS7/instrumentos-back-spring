package com.instrumentos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ItemCarritoDTO {

    @NotNull(message = "El ID del instrumento es obligatorio")
    private Long instrumentoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    // Constructores
    public ItemCarritoDTO() {}

    public ItemCarritoDTO(Long instrumentoId, Integer cantidad) {
        this.instrumentoId = instrumentoId;
        this.cantidad = cantidad;
    }

    // Getters y Setters
    public Long getInstrumentoId() {
        return instrumentoId;
    }

    public void setInstrumentoId(Long instrumentoId) {
        this.instrumentoId = instrumentoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "ItemCarritoDTO{" +
                "instrumentoId=" + instrumentoId +
                ", cantidad=" + cantidad +
                '}';
    }
}
