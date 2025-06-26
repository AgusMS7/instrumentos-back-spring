package com.instrumentos.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.instrumentos.model.Categoria;
import com.instrumentos.repository.CategoriaRepository;

@Service
@Transactional(readOnly = true)
public class CategoriaService {
    
    private static final Logger logger = LoggerFactory.getLogger(CategoriaService.class);
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    public List<Categoria> findAll() {
        logger.debug("Obteniendo todas las categorías");
        return categoriaRepository.findAllOrderByDenominacion();
    }
    
    public Optional<Categoria> findById(Long id) {
        logger.debug("Buscando categoría por ID: {}", id);
        return categoriaRepository.findById(id);
    }
    
    @Transactional
    public Categoria save(Categoria categoria) {
        logger.debug("Guardando categoría: {}", categoria.getDenominacion());
        
        if (categoriaRepository.existsByDenominacionIgnoreCase(categoria.getDenominacion())) {
            throw new RuntimeException("Ya existe una categoría con esa denominación");
        }
        
        try {
            Categoria savedCategoria = categoriaRepository.save(categoria);
            logger.info("Categoría guardada exitosamente con ID: {}", savedCategoria.getId());
            return savedCategoria;
        } catch (DataIntegrityViolationException e) {
            logger.error("Error de integridad al guardar categoría: {}", e.getMessage());
            throw new RuntimeException("Error de integridad de datos al guardar la categoría");
        }
    }
    
    @Transactional
    public Categoria update(Long id, Categoria categoriaDetails) {
        logger.debug("Actualizando categoría ID: {}", id);
        
        return categoriaRepository.findById(id)
                .map(categoria -> {
                    if (!categoria.getDenominacion().equalsIgnoreCase(categoriaDetails.getDenominacion()) &&
                        categoriaRepository.existsByDenominacionIgnoreCase(categoriaDetails.getDenominacion())) {
                        throw new RuntimeException("Ya existe una categoría con esa denominación");
                    }
                    categoria.setDenominacion(categoriaDetails.getDenominacion());
                    
                    try {
                        Categoria updatedCategoria = categoriaRepository.save(categoria);
                        logger.info("Categoría actualizada exitosamente: {}", updatedCategoria.getId());
                        return updatedCategoria;
                    } catch (DataIntegrityViolationException e) {
                        logger.error("Error de integridad al actualizar categoría: {}", e.getMessage());
                        throw new RuntimeException("Error de integridad de datos al actualizar la categoría");
                    }
                })
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
    }
    
    @Transactional
    public void deleteById(Long id) {
        logger.debug("Eliminando categoría ID: {}", id);
        
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada con ID: " + id);
        }
        
        try {
            categoriaRepository.deleteById(id);
            logger.info("Categoría eliminada exitosamente: {}", id);
        } catch (DataIntegrityViolationException e) {
            logger.error("Error de integridad al eliminar categoría: {}", e.getMessage());
            throw new RuntimeException("No se puede eliminar la categoría porque tiene instrumentos asociados");
        }
    }
    
    public List<Categoria> findByDenominacion(String denominacion) {
        logger.debug("Buscando categorías por denominación: {}", denominacion);
        return categoriaRepository.findByDenominacionContainingIgnoreCase(denominacion);
    }
    
    public boolean existsById(Long id) {
        return categoriaRepository.existsById(id);
    }
    
    public long count() {
        return categoriaRepository.count();
    }
}
