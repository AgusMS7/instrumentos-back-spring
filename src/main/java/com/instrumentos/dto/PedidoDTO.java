package com.instrumentos.dto;

import com.instrumentos.model.Pedido;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PedidoDTO {
    
    private Long id;
    private LocalDate fechaPedido;
    private BigDecimal totalPedido;
    private List<PedidoDetalleDTO> detalles = new ArrayList<>();

    // Constructores
    public PedidoDTO() {}

    public PedidoDTO(Long id, LocalDate fechaPedido, BigDecimal totalPedido) {
        this.id = id;
        this.fechaPedido = fechaPedido;
        this.totalPedido = totalPedido;
    }

    public PedidoDTO(Long id, LocalDate fechaPedido, BigDecimal totalPedido, List<PedidoDetalleDTO> detalles) {
        this.id = id;
        this.fechaPedido = fechaPedido;
        this.totalPedido = totalPedido;
        this.detalles = detalles;
    }

    // Método estático para convertir desde entidad
    public static PedidoDTO fromEntity(Pedido pedido) {
        if (pedido == null) {
            return null;
        }
        return new PedidoDTO(
            pedido.getId(),
            pedido.getFechaPedido(),
            pedido.getTotalPedido()
        );
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDate fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public BigDecimal getTotalPedido() {
        return totalPedido;
    }

    public void setTotalPedido(BigDecimal totalPedido) {
        this.totalPedido = totalPedido;
    }

    public List<PedidoDetalleDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<PedidoDetalleDTO> detalles) {
        this.detalles = detalles;
    }

    @Override
    public String toString() {
        return "PedidoDTO{" +
                "id=" + id +
                ", fechaPedido=" + fechaPedido +
                ", totalPedido=" + totalPedido +
                ", detalles=" + detalles.size() +
                '}';
    }
}
