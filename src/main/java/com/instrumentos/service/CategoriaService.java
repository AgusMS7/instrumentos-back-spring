package com.instrumentos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.instrumentos.model.Categoria;
import com.instrumentos.repository.CategoriaRepository;

@Service
@Transactional
public class CategoriaService {
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    // Obtener todas las categorías
    public List<Categoria> findAll() {
        return categoriaRepository.findAllOrderByDenominacion();
    }
    
    // Obtener categoría por ID
    public Optional<Categoria> findById(Long id) {
        return categoriaRepository.findById(id);
    }
    
    // Crear nueva categoría
    public Categoria save(Categoria categoria) {
        if (categoriaRepository.existsByDenominacionIgnoreCase(categoria.getDenominacion())) {
            throw new RuntimeException("Ya existe una categoría con esa denominación");
        }
        return categoriaRepository.save(categoria);
    }
    
    // Actualizar categoría
    public Categoria update(Long id, Categoria categoriaDetails) {
        return categoriaRepository.findById(id)
                .map(categoria -> {
                    // Verificar si la nueva denominación ya existe (excluyendo la actual)
                    if (!categoria.getDenominacion().equalsIgnoreCase(categoriaDetails.getDenominacion()) &&
                        categoriaRepository.existsByDenominacionIgnoreCase(categoriaDetails.getDenominacion())) {
                        throw new RuntimeException("Ya existe una categoría con esa denominación");
                    }
                    categoria.setDenominacion(categoriaDetails.getDenominacion());
                    return categoriaRepository.save(categoria);
                })
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
    }
    
    // Eliminar categoría
    public void deleteById(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada con ID: " + id);
        }
        categoriaRepository.deleteById(id);
    }
    
    // Buscar por denominación
    public List<Categoria> findByDenominacion(String denominacion) {
        return categoriaRepository.findByDenominacionContainingIgnoreCase(denominacion);
    }
    
    // Verificar si existe una categoría
    public boolean existsById(Long id) {
        return categoriaRepository.existsById(id);
    }
    
    // Contar total de categorías
    public long count() {
        return categoriaRepository.count();
    }
}
