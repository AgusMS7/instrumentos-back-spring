package com.instrumentos.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@CrossOrigin(originPatterns = {"http://localhost:*", "https://localhost:*"}, allowCredentials = "true")
@Validated
public class CategoriaController {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaController.class);

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Categoria>>> getAllCategorias() {
        try {
            List<Categoria> categorias = categoriaService.findAll();
            return ResponseEntity.ok(new ApiResponse<>(true, "Categorías obtenidas exitosamente", categorias));
        } catch (Exception e) {
            logger.error("Error al obtener categorías: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Error interno del servidor", null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Categoria>> getCategoriaById(@PathVariable Long id) {
        try {
            return categoriaService.findById(id)
                    .map(categoria -> ResponseEntity.ok(new ApiResponse<>(true, "Categoría obtenida exitosamente", categoria)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Categoría no encontrada", null)));
        } catch (Exception e) {
            logger.error("Error al obtener categoría por ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Error interno del servidor", null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Categoria>> createCategoria(@Valid @RequestBody Categoria categoria) {
        try {
            Categoria savedCategoria = categoriaService.save(categoria);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Categoría creada exitosamente", savedCategoria));
        } catch (RuntimeException e) {
            logger.warn("Error de validación al crear categoría: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("Error al crear categoría: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Error interno del servidor", null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Categoria>> updateCategoria(
            @PathVariable Long id,
            @Valid @RequestBody Categoria categoriaDetails) {
        try {
            Categoria updatedCategoria = categoriaService.update(id, categoriaDetails);
            return ResponseEntity.ok(new ApiResponse<>(true, "Categoría actualizada exitosamente", updatedCategoria));
        } catch (RuntimeException e) {
            logger.warn("Error de validación al actualizar categoría {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("Error al actualizar categoría {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Error interno del servidor", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategoria(@PathVariable Long id) {
        try {
            categoriaService.deleteById(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Categoría eliminada exitosamente", null));
        } catch (RuntimeException e) {
            logger.warn("Error al eliminar categoría {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("Error al eliminar categoría {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Error interno del servidor", null));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Categoria>>> searchCategorias(@RequestParam String denominacion) {
        try {
            List<Categoria> categorias = categoriaService.findByDenominacion(denominacion);
            return ResponseEntity.ok(new ApiResponse<>(true, "Búsqueda completada exitosamente", categorias));
        } catch (Exception e) {
            logger.error("Error en búsqueda de categorías: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Error interno del servidor", null));
        }
    }
}
