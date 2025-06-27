package com.instrumentos.service;

import com.instrumentos.dto.CrearPedidoRequest;
import com.instrumentos.dto.ItemCarritoDTO;
import com.instrumentos.dto.PedidoDTO;
import com.instrumentos.dto.PedidoDetalleDTO;
import com.instrumentos.model.Instrumento;
import com.instrumentos.model.Pedido;
import com.instrumentos.model.PedidoDetalle;
import com.instrumentos.repository.InstrumentoRepository;
import com.instrumentos.repository.PedidoDetalleRepository;
import com.instrumentos.repository.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PedidoService {

    private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoDetalleRepository pedidoDetalleRepository;

    @Autowired
    private InstrumentoRepository instrumentoRepository;

    @Transactional
    public PedidoDTO crearPedido(CrearPedidoRequest request) {
        try {
            logger.debug("Creando pedido con {} instrumentos", request.getInstrumentos().size());

            // Validar que todos los instrumentos existan
            for (ItemCarritoDTO item : request.getInstrumentos()) {
                if (!instrumentoRepository.existsById(item.getInstrumentoId())) {
                    throw new RuntimeException("Instrumento no encontrado con ID: " + item.getInstrumentoId());
                }
                if (item.getCantidad() <= 0) {
                    throw new RuntimeException("La cantidad debe ser mayor a 0");
                }
            }

            // Crear el pedido
            Pedido pedido = new Pedido();
            pedido.setFechaPedido(LocalDate.now());

            // Calcular total
            BigDecimal total = BigDecimal.ZERO;
            for (ItemCarritoDTO item : request.getInstrumentos()) {
                Optional<Instrumento> instrumentoOpt = instrumentoRepository.findById(item.getInstrumentoId());
                if (instrumentoOpt.isPresent()) {
                    Instrumento instrumento = instrumentoOpt.get();
                    BigDecimal subtotal = instrumento.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad()));
                    total = total.add(subtotal);
                }
            }

            pedido.setTotalPedido(total);

            // Guardar pedido
            Pedido pedidoGuardado = pedidoRepository.save(pedido);
            logger.info("Pedido creado con ID: {}", pedidoGuardado.getId());

            // Crear y guardar detalles
            for (ItemCarritoDTO item : request.getInstrumentos()) {
                Optional<Instrumento> instrumentoOpt = instrumentoRepository.findById(item.getInstrumentoId());
                if (instrumentoOpt.isPresent()) {
                    PedidoDetalle detalle = new PedidoDetalle();
                    detalle.setPedido(pedidoGuardado);
                    detalle.setInstrumento(instrumentoOpt.get());
                    detalle.setCantidad(item.getCantidad());
                    
                    pedidoDetalleRepository.save(detalle);
                }
            }

            return convertToDTO(pedidoGuardado);

        } catch (RuntimeException e) {
            logger.error("Error de negocio al crear pedido: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error al crear pedido: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear pedido: " + e.getMessage(), e);
        }
    }

    public List<PedidoDTO> findAll() {
        try {
            logger.debug("Obteniendo todos los pedidos");
            List<Pedido> pedidos = pedidoRepository.findAllWithDetalles();
            return pedidos.stream()
                    .map(this::convertToDTOWithDetalles)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error al obtener pedidos: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener pedidos: " + e.getMessage(), e);
        }
    }

    public Optional<PedidoDTO> findById(Long id) {
        try {
            logger.debug("Buscando pedido por ID: {}", id);
            Optional<Pedido> pedidoOpt = pedidoRepository.findByIdWithDetalles(id);
            return pedidoOpt.map(this::convertToDTOWithDetalles);
        } catch (Exception e) {
            logger.error("Error al obtener pedido por ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error al obtener pedido por ID: " + e.getMessage(), e);
        }
    }

    public List<PedidoDTO> findByFecha(LocalDate fecha) {
        try {
            logger.debug("Buscando pedidos por fecha: {}", fecha);
            List<Pedido> pedidos = pedidoRepository.findByFechaPedidoWithDetalles(fecha);
            return pedidos.stream()
                    .map(this::convertToDTOWithDetalles)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error al buscar pedidos por fecha {}: {}", fecha, e.getMessage(), e);
            throw new RuntimeException("Error al buscar pedidos por fecha: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        try {
            logger.debug("Eliminando pedido ID: {}", id);
            
            if (!pedidoRepository.existsById(id)) {
                throw new RuntimeException("Pedido no encontrado con ID: " + id);
            }
            
            pedidoRepository.deleteById(id);
            logger.info("Pedido eliminado exitosamente: {}", id);
            
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error al eliminar pedido {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error al eliminar pedido: " + e.getMessage(), e);
        }
    }

    public boolean existsById(Long id) {
        try {
            return pedidoRepository.existsById(id);
        } catch (Exception e) {
            logger.error("Error al verificar existencia del pedido {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error al verificar existencia del pedido: " + e.getMessage(), e);
        }
    }

    private PedidoDTO convertToDTO(Pedido pedido) {
        if (pedido == null) {
            return null;
        }
        return PedidoDTO.fromEntity(pedido);
    }

    private PedidoDTO convertToDTOWithDetalles(Pedido pedido) {
        if (pedido == null) {
            return null;
        }
        
        PedidoDTO dto = PedidoDTO.fromEntity(pedido);
        
        if (pedido.getDetalles() != null && !pedido.getDetalles().isEmpty()) {
            List<PedidoDetalleDTO> detallesDTO = pedido.getDetalles().stream()
                    .map(PedidoDetalleDTO::fromEntity)
                    .collect(Collectors.toList());
            dto.setDetalles(detallesDTO);
        }
        
        return dto;
    }
}
