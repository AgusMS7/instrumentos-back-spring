package com.instrumentos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.instrumentos.model.Categoria;
import com.instrumentos.model.Instrumento;
import com.instrumentos.repository.InstrumentoRepository;

@Service
@Transactional
public class InstrumentoService {
    
    @Autowired
    private InstrumentoRepository instrumentoRepository;

    @Autowired
    private CategoriaService categoriaService;
    
    // Obtener todos los instrumentos
    public List<Instrumento> findAll() {
        return instrumentoRepository.findAll();
    }
    
    // Obtener todos los instrumentos con paginación
    public List<Instrumento> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Instrumento> pageResult = instrumentoRepository.findAll(pageable);
        return pageResult.getContent();
    }
    
    // Obtener instrumento por ID
    public Optional<Instrumento> findById(Long id) {
        return instrumentoRepository.findById(id);
    }
    
    // Crear nuevo instrumento
    public Instrumento save(Instrumento instrumento) {
        // Validar y asignar categoría si se proporciona
        if (instrumento.getCategoria() != null && instrumento.getCategoria().getId() != null) {
            Categoria categoria = categoriaService.findById(instrumento.getCategoria().getId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            instrumento.setCategoria(categoria);
        }
        return instrumentoRepository.save(instrumento);
    }
    
    // Actualizar instrumento
    public Instrumento update(Long id, Instrumento instrumentoDetails) {
        return instrumentoRepository.findById(id)
                .map(instrumento -> {
                    instrumento.setInstrumento(instrumentoDetails.getInstrumento());
                    instrumento.setMarca(instrumentoDetails.getMarca());
                    instrumento.setModelo(instrumentoDetails.getModelo());
                    instrumento.setPrecio(instrumentoDetails.getPrecio());
                    instrumento.setCostoEnvio(instrumentoDetails.getCostoEnvio());
                    instrumento.setCantidadVendida(instrumentoDetails.getCantidadVendida());
                    instrumento.setDescripcion(instrumentoDetails.getDescripcion());
                    instrumento.setImagen(instrumentoDetails.getImagen());
                    
                    // Validar y asignar categoría
                    if (instrumentoDetails.getCategoria() != null && instrumentoDetails.getCategoria().getId() != null) {
                        Categoria categoria = categoriaService.findById(instrumentoDetails.getCategoria().getId())
                                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
                        instrumento.setCategoria(categoria);
                    }
                    
                    return instrumentoRepository.save(instrumento);
                })
                .orElseThrow(() -> new RuntimeException("Instrumento no encontrado con ID: " + id));
    }
    
    // Eliminar instrumento
    public void deleteById(Long id) {
        if (!instrumentoRepository.existsById(id)) {
            throw new RuntimeException("Instrumento no encontrado con ID: " + id);
        }
        instrumentoRepository.deleteById(id);
    }
    
    // Buscar por marca
    public List<Instrumento> findByMarca(String marca) {
        return instrumentoRepository.findByMarcaContainingIgnoreCase(marca);
    }
    
    // Buscar por nombre de instrumento
    public List<Instrumento> findByInstrumento(String instrumento) {
        return instrumentoRepository.findByInstrumentoContainingIgnoreCase(instrumento);
    }
    
    // Buscar por múltiples criterios
    public List<Instrumento> findByMultipleCriteria(String marca, String instrumento, String modelo) {
        return instrumentoRepository.findByMultipleCriteria(marca, instrumento, modelo);
    }
    
    // Obtener instrumentos con envío gratis
    public List<Instrumento> findWithFreeShipping() {
        return instrumentoRepository.findByCostoEnvio("G");
    }
    
    // Verificar si existe un instrumento
    public boolean existsById(Long id) {
        return instrumentoRepository.existsById(id);
    }
    
    // Contar total de instrumentos
    public long count() {
        return instrumentoRepository.count();
    }

    // Buscar instrumentos por categoría
    public List<Instrumento> findByCategoria(Long categoriaId) {
        return instrumentoRepository.findByCategoriaId(categoriaId);
    }

    // Buscar por múltiples criterios incluyendo categoría
    public List<Instrumento> findByMultipleCriteriaWithCategory(String marca, String instrumento, String modelo, Long categoriaId) {
        return instrumentoRepository.findByMultipleCriteriaWithCategory(marca, instrumento, modelo, categoriaId);
    }
    
    // Obtener estadísticas por marca
    public List<Object[]> getStatsByMarca() {
        return instrumentoRepository.countByMarca();
    }
}
