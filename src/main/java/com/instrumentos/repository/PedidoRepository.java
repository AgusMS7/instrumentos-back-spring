package com.instrumentos.repository;

import com.instrumentos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.detalles d LEFT JOIN FETCH d.instrumento ORDER BY p.id DESC")
    List<Pedido> findAllWithDetalles();

    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.detalles d LEFT JOIN FETCH d.instrumento WHERE p.id = :id")
    Optional<Pedido> findByIdWithDetalles(@Param("id") Long id);

    @Query("SELECT p FROM Pedido p WHERE p.fechaPedido = :fecha ORDER BY p.id DESC")
    List<Pedido> findByFechaPedido(@Param("fecha") LocalDate fecha);

    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.detalles d LEFT JOIN FETCH d.instrumento WHERE p.fechaPedido = :fecha ORDER BY p.id DESC")
    List<Pedido> findByFechaPedidoWithDetalles(@Param("fecha") LocalDate fecha);
}
