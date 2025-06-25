package com.instrumentos.controller;

import java.util.List;

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
import com.instrumentos.model.Categoria;
import com.instrumentos.service.CategoriaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
@Validated
public class CategoriaController {
    
    @Autowired
    private CategoriaService categoriaService;
    
    // GET /api/categorias - Obtener todas las categorías
    @GetMapping
    public ResponseEntity<List<Categoria>> getAllCategorias() {
        try {
            List<Categoria> categorias = categoriaService.findAll();
            return ResponseEntity.ok(categorias);
        } catch (Exception e) {
            System.err.println("Error al obtener categorías: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // GET /api/categorias/{id} - Obtener categoría por ID
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getCategoriaById(@PathVariable Long id) {
        try {
            return categoriaService.findById(id)
                    .map(categoria -> ResponseEntity.ok(categoria))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            System.err.println("Error al obtener categoría por ID: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // POST /api/categorias - Crear nueva categoría
    @PostMapping
    public ResponseEntity<ApiResponse<Categoria>> createCategoria(@Valid @RequestBody Categoria categoria) {
        try {
            Categoria savedCategoria = categoriaService.save(categoria);
            ApiResponse<Categoria> response = new ApiResponse<>(
                    true, 
                    "Categoría creada exitosamente", 
                    savedCategoria
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            ApiResponse<Categoria> response = new ApiResponse<>(
                    false, 
                    e.getMessage(), 
                    null
            );
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            System.err.println("Error al crear categoría: " + e.getMessage());
            ApiResponse<Categoria> response = new ApiResponse<>(
                    false, 
                    "Error interno del servidor", 
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // PUT /api/categorias/{id} - Actualizar categoría
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Categoria>> updateCategoria(
            @PathVariable Long id, 
            @Valid @RequestBody Categoria categoriaDetails) {
        try {
            Categoria updatedCategoria = categoriaService.update(id, categoriaDetails);
            ApiResponse<Categoria> response = new ApiResponse<>(
                    true, 
                    "Categoría actualizada exitosamente", 
                    updatedCategoria
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<Categoria> response = new ApiResponse<>(
                    false, 
                    e.getMessage(), 
                    null
            );
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            System.err.println("Error al actualizar categoría: " + e.getMessage());
            ApiResponse<Categoria> response = new ApiResponse<>(
                    false, 
                    "Error interno del servidor", 
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // DELETE /api/categorias/{id} - Eliminar categoría
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategoria(@PathVariable Long id) {
        try {
            categoriaService.deleteById(id);
            ApiResponse<Void> response = new ApiResponse<>(
                    true, 
                    "Categoría eliminada exitosamente", 
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
            System.err.println("Error al eliminar categoría: " + e.getMessage());
            ApiResponse<Void> response = new ApiResponse<>(
                    false, 
                    "Error interno del servidor", 
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // GET /api/categorias/search - Buscar categorías
    @GetMapping("/search")
    public ResponseEntity<List<Categoria>> searchCategorias(@RequestParam String denominacion) {
        try {
            List<Categoria> categorias = categoriaService.findByDenominacion(denominacion);
            return ResponseEntity.ok(categorias);
        } catch (Exception e) {
            System.err.println("Error en búsqueda de categorías: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
