package com.instrumentos.controller;

import com.instrumentos.dto.ApiResponse;
import com.instrumentos.dto.CrearPedidoRequest;
import com.instrumentos.dto.PedidoDTO;
import com.instrumentos.service.PedidoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class PedidoController {

    private static final Logger logger = LoggerFactory.getLogger(PedidoController.class);

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<ApiResponse<PedidoDTO>> crearPedido(@Valid @RequestBody CrearPedidoRequest request) {
        logger.info("Recibida solicitud para crear pedido con {} instrumentos", 
                   request.getInstrumentos() != null ? request.getInstrumentos().size() : 0);
        
        try {
            PedidoDTO pedidoCreado = pedidoService.crearPedido(request);
            
            String mensaje = String.format("El pedido con id %d se guardó correctamente", pedidoCreado.getId());
            ApiResponse<PedidoDTO> response = new ApiResponse<>(true, mensaje, pedidoCreado);
            
            logger.info("Pedido creado exitosamente con ID: {}", pedidoCreado.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (RuntimeException e) {
            logger.warn("Error de validación al crear pedido: {}", e.getMessage());
            ApiResponse<PedidoDTO> response = new ApiResponse<>(false, e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
            
        } catch (Exception e) {
            logger.error("Error interno al crear pedido: {}", e.getMessage(), e);
            ApiResponse<PedidoDTO> response = new ApiResponse<>(false, "Error interno del servidor", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PedidoDTO>>> getAllPedidos() {
        logger.info("Recibida solicitud para obtener todos los pedidos");
        
        try {
            List<PedidoDTO> pedidos = pedidoService.findAll();
            
            String mensaje = String.format("Se encontraron %d pedidos", pedidos.size());
            ApiResponse<List<PedidoDTO>> response = new ApiResponse<>(true, mensaje, pedidos);
            
            logger.info("Devolviendo {} pedidos", pedidos.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error al obtener todos los pedidos: {}", e.getMessage(), e);
            ApiResponse<List<PedidoDTO>> response = new ApiResponse<>(false, "Error al obtener los pedidos", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PedidoDTO>> getPedidoById(@PathVariable Long id) {
        logger.info("Recibida solicitud para obtener pedido con ID: {}", id);
        
        try {
            Optional<PedidoDTO> pedidoOpt = pedidoService.findById(id);
            
            if (pedidoOpt.isPresent()) {
                PedidoDTO pedido = pedidoOpt.get();
                String mensaje = String.format("Pedido con ID %d encontrado", id);
                ApiResponse<PedidoDTO> response = new ApiResponse<>(true, mensaje, pedido);
                
                logger.info("Pedido con ID {} encontrado", id);
                return ResponseEntity.ok(response);
            } else {
                String mensaje = String.format("Pedido con ID %d no encontrado", id);
                ApiResponse<PedidoDTO> response = new ApiResponse<>(false, mensaje, null);
                
                logger.warn("Pedido con ID {} no encontrado", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
        } catch (Exception e) {
            logger.error("Error al obtener pedido con ID {}: {}", id, e.getMessage(), e);
            ApiResponse<PedidoDTO> response = new ApiResponse<>(false, "Error al obtener el pedido", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/fecha")
    public ResponseEntity<ApiResponse<List<PedidoDTO>>> getPedidosByFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        logger.info("Recibida solicitud para obtener pedidos de la fecha: {}", fecha);
        
        try {
            List<PedidoDTO> pedidos = pedidoService.findByFecha(fecha);
            
            String mensaje = String.format("Se encontraron %d pedidos para la fecha %s", pedidos.size(), fecha);
            ApiResponse<List<PedidoDTO>> response = new ApiResponse<>(true, mensaje, pedidos);
            
            logger.info("Devolviendo {} pedidos para la fecha {}", pedidos.size(), fecha);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error al obtener pedidos por fecha {}: {}", fecha, e.getMessage(), e);
            ApiResponse<List<PedidoDTO>> response = new ApiResponse<>(false, "Error al obtener pedidos por fecha", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePedido(@PathVariable Long id) {
        logger.info("Recibida solicitud para eliminar pedido con ID: {}", id);
        
        try {
            if (!pedidoService.existsById(id)) {
                String mensaje = String.format("Pedido con ID %d no encontrado", id);
                ApiResponse<Void> response = new ApiResponse<>(false, mensaje, null);
                
                logger.warn("Pedido con ID {} no encontrado para eliminar", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            pedidoService.deleteById(id);
            
            String mensaje = String.format("Pedido con ID %d eliminado correctamente", id);
            ApiResponse<Void> response = new ApiResponse<>(true, mensaje, null);
            
            logger.info("Pedido con ID {} eliminado exitosamente", id);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error al eliminar pedido con ID {}: {}", id, e.getMessage(), e);
            ApiResponse<Void> response = new ApiResponse<>(false, "Error al eliminar el pedido", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
