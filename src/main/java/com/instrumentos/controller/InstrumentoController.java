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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/instrumentos")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
@Validated
public class InstrumentoController {
    
    @Autowired
    private InstrumentoService instrumentoService;
    
    // GET /api/instrumentos - Obtener todos los instrumentos
    @GetMapping
    public ResponseEntity<List<InstrumentoDTO>> getAllInstrumentos(HttpServletRequest request) {
        try {
            List<Instrumento> instrumentos = instrumentoService.findAll();
            
            if (instrumentos.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            String baseUrl = getBaseUrl(request);
            List<InstrumentoDTO> instrumentosDTO = instrumentos.stream()
                    .map(instrumento -> InstrumentoDTO.fromEntity(instrumento, baseUrl))
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(instrumentosDTO);
            
        } catch (Exception e) {
            System.err.println("Error al obtener instrumentos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // GET /api/instrumentos/{id} - Obtener instrumento por ID
    @GetMapping("/{id}")
    public ResponseEntity<InstrumentoDTO> getInstrumentoById(@PathVariable Integer id, HttpServletRequest request) {
        try {
            return instrumentoService.findById(id)
                    .map(instrumento -> {
                        String baseUrl = getBaseUrl(request);
                        InstrumentoDTO dto = InstrumentoDTO.fromEntity(instrumento, baseUrl);
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
            @Valid @RequestBody Instrumento instrumento, 
            HttpServletRequest request) {
        try {
            Instrumento savedInstrumento = instrumentoService.save(instrumento);
            String baseUrl = getBaseUrl(request);
            InstrumentoDTO dto = InstrumentoDTO.fromEntity(savedInstrumento, baseUrl);
            
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
            @PathVariable Integer id, 
            @Valid @RequestBody Instrumento instrumentoDetails,
            HttpServletRequest request) {
        try {
            Instrumento updatedInstrumento = instrumentoService.update(id, instrumentoDetails);
            String baseUrl = getBaseUrl(request);
            InstrumentoDTO dto = InstrumentoDTO.fromEntity(updatedInstrumento, baseUrl);
            
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
            return ResponseEntity.notFound().build();
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
    public ResponseEntity<ApiResponse<Void>> deleteInstrumento(@PathVariable Integer id) {
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
            return ResponseEntity.notFound().build();
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
            HttpServletRequest request) {
        try {
            List<Instrumento> instrumentos = instrumentoService.findByMultipleCriteria(marca, instrumento, modelo);
            String baseUrl = getBaseUrl(request);
            List<InstrumentoDTO> instrumentosDTO = instrumentos.stream()
                    .map(inst -> InstrumentoDTO.fromEntity(inst, baseUrl))
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(instrumentosDTO);
            
        } catch (Exception e) {
            System.err.println("Error en búsqueda: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // GET /api/instrumentos/free-shipping - Instrumentos con envío gratis
    @GetMapping("/free-shipping")
    public ResponseEntity<List<InstrumentoDTO>> getInstrumentosWithFreeShipping(HttpServletRequest request) {
        try {
            List<Instrumento> instrumentos = instrumentoService.findWithFreeShipping();
            String baseUrl = getBaseUrl(request);
            List<InstrumentoDTO> instrumentosDTO = instrumentos.stream()
                    .map(inst -> InstrumentoDTO.fromEntity(inst, baseUrl))
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(instrumentosDTO);
            
        } catch (Exception e) {
            System.err.println("Error al obtener instrumentos con envío gratis: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Método helper para construir URL base
    private String getBaseUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }
}
