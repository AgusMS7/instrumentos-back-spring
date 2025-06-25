package com.instrumentos.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
@Validated
public class InstrumentoController {
    
    @Autowired
    private InstrumentoService instrumentoService;
    
    // GET /api/instrumentos - Con paginación opcional
    @GetMapping
    public ResponseEntity<List<InstrumentoDTO>> getAllInstrumentos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        try {
            List<Instrumento> instrumentos = instrumentoService.findAll(page, size);
            List<InstrumentoDTO> instrumentosDTO = instrumentos.stream()
                    .map(InstrumentoDTO::fromEntity)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(instrumentosDTO);
        } catch (Exception e) {
            System.err.println("Error al obtener instrumentos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // GET /api/instrumentos/{id} - Obtener instrumento por ID
    @GetMapping("/{id}")
    public ResponseEntity<InstrumentoDTO> getInstrumentoById(@PathVariable Long id) {
        try {
            return instrumentoService.findById(id)
                    .map(instrumento -> {
                        InstrumentoDTO dto = InstrumentoDTO.fromEntity(instrumento);
                        return ResponseEntity.ok(dto);
                    })
                    .orElse(ResponseEntity.notFound().build());
                    
        } catch (Exception e) {
            System.err.println("Error al obtener instrumento por ID: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // POST /api/instrumentos - Crear nuevo instrumento
    @PostMapping
    public ResponseEntity<ApiResponse<InstrumentoDTO>> createInstrumento(
            @Valid @RequestBody Instrumento instrumento) {
        try {
            Instrumento savedInstrumento = instrumentoService.save(instrumento);
            InstrumentoDTO dto = InstrumentoDTO.fromEntity(savedInstrumento);
            
            ApiResponse<InstrumentoDTO> response = new ApiResponse<>(
                    true, 
                    "Instrumento creado exitosamente", 
                    dto
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            System.err.println("Error al crear instrumento: " + e.getMessage());
            ApiResponse<InstrumentoDTO> response = new ApiResponse<>(
                    false, 
                    "Error interno del servidor: " + e.getMessage(), 
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // PUT /api/instrumentos/{id} - Actualizar instrumento
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InstrumentoDTO>> updateInstrumento(
            @PathVariable Long id, 
            @Valid @RequestBody Instrumento instrumentoDetails) {
        try {
            Instrumento updatedInstrumento = instrumentoService.update(id, instrumentoDetails);
            InstrumentoDTO dto = InstrumentoDTO.fromEntity(updatedInstrumento);
            
            ApiResponse<InstrumentoDTO> response = new ApiResponse<>(
                    true, 
                    "Instrumento actualizado exitosamente", 
                    dto
            );
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            ApiResponse<InstrumentoDTO> response = new ApiResponse<>(
                    false, 
                    e.getMessage(), 
                    null
            );
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            System.err.println("Error al actualizar instrumento: " + e.getMessage());
            ApiResponse<InstrumentoDTO> response = new ApiResponse<>(
                    false, 
                    "Error interno del servidor", 
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // DELETE /api/instrumentos/{id} - Eliminar instrumento
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteInstrumento(@PathVariable Long id) {
        try {
            instrumentoService.deleteById(id);
            ApiResponse<Void> response = new ApiResponse<>(
                    true, 
                    "Instrumento eliminado exitosamente", 
                    null
            );
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            ApiResponse<Void> response = new ApiResponse<>(
                    false, 
                    e.getMessage(), 
                    null
            );
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            System.err.println("Error al eliminar instrumento: " + e.getMessage());
            ApiResponse<Void> response = new ApiResponse<>(
                    false, 
                    "Error interno del servidor", 
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // GET /api/instrumentos/search - Buscar instrumentos
    @GetMapping("/search")
    public ResponseEntity<List<InstrumentoDTO>> searchInstrumentos(
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String instrumento,
            @RequestParam(required = false) String modelo,
            @RequestParam(required = false) Long categoriaId) {
        try {
            List<Instrumento> instrumentos = instrumentoService.findByMultipleCriteriaWithCategory(marca, instrumento, modelo, categoriaId);
            List<InstrumentoDTO> instrumentosDTO = instrumentos.stream()
                    .map(InstrumentoDTO::fromEntity)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(instrumentosDTO);
            
        } catch (Exception e) {
            System.err.println("Error en búsqueda: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // GET /api/instrumentos/free-shipping - Instrumentos con envío gratis
    @GetMapping("/free-shipping")
    public ResponseEntity<List<InstrumentoDTO>> getInstrumentosWithFreeShipping() {
        try {
            List<Instrumento> instrumentos = instrumentoService.findWithFreeShipping();
            List<InstrumentoDTO> instrumentosDTO = instrumentos.stream()
                    .map(InstrumentoDTO::fromEntity)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(instrumentosDTO);
            
        } catch (Exception e) {
            System.err.println("Error al obtener instrumentos con envío gratis: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET /api/instrumentos/categoria/{categoriaId} - Filtrar por categoría
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<InstrumentoDTO>> getInstrumentosByCategoria(
            @PathVariable Long categoriaId) {
        try {
            List<Instrumento> instrumentos = instrumentoService.findByCategoria(categoriaId);
            List<InstrumentoDTO> instrumentosDTO = instrumentos.stream()
                    .map(InstrumentoDTO::fromEntity)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(instrumentosDTO);
        } catch (Exception e) {
            System.err.println("Error al obtener instrumentos por categoría: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
