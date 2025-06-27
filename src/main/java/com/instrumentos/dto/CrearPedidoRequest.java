package com.instrumentos.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CrearPedidoRequest {

    @NotNull(message = "La lista de instrumentos no puede ser nula")
    @NotEmpty(message = "Debe incluir al menos un instrumento")
    @Valid
    private List<ItemCarritoDTO> instrumentos;

    // Constructores
    public CrearPedidoRequest() {}

    public CrearPedidoRequest(List<ItemCarritoDTO> instrumentos) {
        this.instrumentos = instrumentos;
    }

    // Getters y Setters
    public List<ItemCarritoDTO> getInstrumentos() {
        return instrumentos;
    }

    public void setInstrumentos(List<ItemCarritoDTO> instrumentos) {
        this.instrumentos = instrumentos;
    }

    @Override
    public String toString() {
        return "CrearPedidoRequest{" +
                "instrumentos=" + (instrumentos != null ? instrumentos.size() : 0) +
                '}';
    }
}
