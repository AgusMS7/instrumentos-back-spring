package com.instrumentos.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.instrumentos.dto.ApiResponse;
import com.instrumentos.dto.InstrumentoDTO;
import com.instrumentos.model.Instrumento;
import com.instrumentos.service.InstrumentoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/instrumentos")
@CrossOrigin(originPatterns = {"http://localhost:*", "https://localhost:*"}, allowCredentials = "true")
@Validated
public class InstrumentoController {

    private static final Logger logger = LoggerFactory.getLogger(InstrumentoController.class);

    @Autowired
    private InstrumentoService instrumentoService;

    @GetMapping
    public ResponseEntity<?> getAllInstrumentos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "false") boolean paginated) {
        
        try {
            if (paginated) {
                Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                    Sort.by(sortBy).descending() : 
                    Sort.by(sortBy).ascending();
                
                Pageable pageable = PageRequest.of(page, size, sort);
                Page<InstrumentoDTO> instrumentos = instrumentoService.findAllPaginated(pageable);
                
                return ResponseEntity.ok(instrumentos);
            } else {
                List<InstrumentoDTO> instrumentos = instrumentoService.findAll();
                return ResponseEntity.ok(new ApiResponse<>(true, "Instrumentos obtenidos exitosamente", instrumentos));
            }
        } catch (Exception e) {
            logger.error("Error al obtener instrumentos: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Error al obtener instrumentos: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InstrumentoDTO>> getInstrumentoById(@PathVariable Long id) {
        try {
            Optional<InstrumentoDTO> instrumento = instrumentoService.findById(id);
            
            if (instrumento.isPresent()) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Instrumento encontrado", instrumento.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Instrumento no encontrado", null));
            }
        } catch (Exception e) {
            logger.error("Error al obtener instrumento por ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Error al obtener instrumento: " + e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InstrumentoDTO>> createInstrumento(@Valid @RequestBody Instrumento instrumento) {
        try {
            InstrumentoDTO nuevoInstrumento = instrumentoService.save(instrumento);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Instrumento creado exitosamente", nuevoInstrumento));
        } catch (Exception e) {
            logger.error("Error al crear instrumento: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, "Error al crear instrumento: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InstrumentoDTO>> updateInstrumento(
            @PathVariable Long id, 
            @Valid @RequestBody Instrumento instrumento) {
        try {
            if (!instrumentoService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Instrumento no encontrado", null));
            }
            
            instrumento.setId(id);
            InstrumentoDTO instrumentoActualizado = instrumentoService.save(instrumento);
            return ResponseEntity.ok(new ApiResponse<>(true, "Instrumento actualizado exitosamente", instrumentoActualizado));
        } catch (Exception e) {
            logger.error("Error al actualizar instrumento {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, "Error al actualizar instrumento: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteInstrumento(@PathVariable Long id) {
        try {
            if (instrumentoService.existsById(id)) {
                instrumentoService.deleteById(id);
                return ResponseEntity.ok(new ApiResponse<>(true, "Instrumento eliminado exitosamente", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Instrumento no encontrado", null));
            }
        } catch (Exception e) {
            logger.error("Error al eliminar instrumento {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Error al eliminar instrumento: " + e.getMessage(), null));
        }
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<ApiResponse<List<InstrumentoDTO>>> getInstrumentosByCategoria(@PathVariable Long categoriaId) {
        try {
            List<InstrumentoDTO> instrumentos = instrumentoService.findByCategoriaId(categoriaId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Instrumentos filtrados exitosamente", instrumentos));
        } catch (Exception e) {
            logger.error("Error al filtrar instrumentos por categoría {}: {}", categoriaId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Error al filtrar instrumentos: " + e.getMessage(), null));
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<InstrumentoDTO>>> buscarInstrumentos(@RequestParam String nombre) {
        try {
            List<InstrumentoDTO> instrumentos = instrumentoService.findByInstrumentoContainingIgnoreCase(nombre);
            return ResponseEntity.ok(new ApiResponse<>(true, "Búsqueda completada exitosamente", instrumentos));
        } catch (Exception e) {
            logger.error("Error al buscar instrumentos con nombre '{}': {}", nombre, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Error al buscar instrumentos: " + e.getMessage(), null));
        }
    }
}
