package com.instrumentos.service;

import com.instrumentos.model.Instrumento;
import com.instrumentos.model.ProductImage;
import com.instrumentos.repository.InstrumentoRepository;
import com.instrumentos.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InstrumentoService {
    
    @Autowired
    private InstrumentoRepository instrumentoRepository;
    
    @Autowired
    private ProductImageRepository productImageRepository;
    
    // Obtener todos los instrumentos
    public List<Instrumento> findAll() {
        return instrumentoRepository.findAllWithImages();
    }
    
    // Obtener instrumento por ID
    public Optional<Instrumento> findById(Integer id) {
        return instrumentoRepository.findByIdWithImages(id);
    }
    
    // Crear nuevo instrumento
    public Instrumento save(Instrumento instrumento) {
        return instrumentoRepository.save(instrumento);
    }
    
    // Actualizar instrumento
    public Instrumento update(Integer id, Instrumento instrumentoDetails) {
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
                    return instrumentoRepository.save(instrumento);
                })
                .orElseThrow(() -> new RuntimeException("Instrumento no encontrado con ID: " + id));
    }
    
    // Eliminar instrumento
    public void deleteById(Integer id) {
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
    public boolean existsById(Integer id) {
        return instrumentoRepository.existsById(id);
    }
    
    // Contar total de instrumentos
    public long count() {
        return instrumentoRepository.count();
    }
    
    // Obtener estadísticas por marca
    public List<Object[]> getStatsByMarca() {
        return instrumentoRepository.countByMarca();
    }
}
