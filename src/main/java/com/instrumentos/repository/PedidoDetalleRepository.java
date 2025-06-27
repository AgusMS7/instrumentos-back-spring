package com.instrumentos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.instrumentos.model.PedidoDetalle;

@Repository
public interface PedidoDetalleRepository extends JpaRepository<PedidoDetalle, Long> {
    
    // Buscar detalles por pedido
    @Query("SELECT pd FROM PedidoDetalle pd WHERE pd.pedido.id = :pedidoId")
    List<PedidoDetalle> findByPedidoId(@Param("pedidoId") Long pedidoId);
    
    // Buscar detalles por instrumento
    @Query("SELECT pd FROM PedidoDetalle pd WHERE pd.instrumento.id = :instrumentoId")
    List<PedidoDetalle> findByInstrumentoId(@Param("instrumentoId") Long instrumentoId);
    
    // Buscar detalles con información completa
    @Query("SELECT pd FROM PedidoDetalle pd JOIN FETCH pd.instrumento WHERE pd.pedido.id = :pedidoId")
    List<PedidoDetalle> findByPedidoIdWithInstrumento(@Param("pedidoId") Long pedidoId);
    
    // Contar detalles por pedido
    long countByPedidoId(Long pedidoId);
    
    // Sumar cantidad total por instrumento
    @Query("SELECT COALESCE(SUM(pd.cantidad), 0) FROM PedidoDetalle pd WHERE pd.instrumento.id = :instrumentoId")
    Integer getTotalCantidadVendidaPorInstrumento(@Param("instrumentoId") Long instrumentoId);
    
    // Obtener los instrumentos más vendidos
    @Query("SELECT pd.instrumento.id, pd.instrumento.instrumento, SUM(pd.cantidad) as total " +
           "FROM PedidoDetalle pd " +
           "GROUP BY pd.instrumento.id, pd.instrumento.instrumento " +
           "ORDER BY total DESC")
    List<Object[]> getInstrumentosMasVendidos();
}
