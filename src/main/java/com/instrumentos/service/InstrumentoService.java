package com.instrumentos.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.instrumentos.dto.InstrumentoDTO;
import com.instrumentos.model.Instrumento;
import com.instrumentos.repository.InstrumentoRepository;

@Service
@Transactional(readOnly = true)
public class InstrumentoService {

    @Autowired
    private InstrumentoRepository instrumentoRepository;

    @Autowired
    private ProductImageService productImageService;

    // Obtener todos los instrumentos
    public List<InstrumentoDTO> findAll() {
        try {
            List<Instrumento> instrumentos = instrumentoRepository.findAll();
            return instrumentos.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener instrumentos: " + e.getMessage(), e);
        }
    }

    // Obtener todos con paginación
    public Page<InstrumentoDTO> findAllPaginated(Pageable pageable) {
        try {
            Page<Instrumento> instrumentosPage = instrumentoRepository.findAll(pageable);
            List<InstrumentoDTO> instrumentosDTO = instrumentosPage.getContent().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            
            return new PageImpl<>(instrumentosDTO, pageable, instrumentosPage.getTotalElements());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener instrumentos paginados: " + e.getMessage(), e);
        }
    }

    // Obtener por ID
    public Optional<InstrumentoDTO> findById(Long id) {
        try {
            return instrumentoRepository.findById(id)
                    .map(this::convertToDTO);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener instrumento por ID: " + e.getMessage(), e);
        }
    }

    // Guardar instrumento
    @Transactional
    public InstrumentoDTO save(Instrumento instrumento) {
        try {
            Instrumento savedInstrumento = instrumentoRepository.save(instrumento);
            return convertToDTO(savedInstrumento);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar instrumento: " + e.getMessage(), e);
        }
    }

    // Eliminar por ID
    @Transactional
    public void deleteById(Long id) {
        try {
            // Primero eliminar las imágenes asociadas
            productImageService.deleteImagesByInstrumento(id);
            
            // Luego eliminar el instrumento
            instrumentoRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar instrumento: " + e.getMessage(), e);
        }
    }

    // Verificar si existe
    public boolean existsById(Long id) {
        try {
            return instrumentoRepository.existsById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar existencia del instrumento: " + e.getMessage(), e);
        }
    }

    // Buscar por categoría
    public List<InstrumentoDTO> findByCategoriaId(Long categoriaId) {
        try {
            List<Instrumento> instrumentos = instrumentoRepository.findByCategoriaId(categoriaId);
            return instrumentos.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al filtrar instrumentos por categoría: " + e.getMessage(), e);
        }
    }

    // Buscar por nombre (contiene)
    public List<InstrumentoDTO> findByInstrumentoContainingIgnoreCase(String nombre) {
        try {
            List<Instrumento> instrumentos = instrumentoRepository.findByInstrumentoContainingIgnoreCase(nombre);
            return instrumentos.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar instrumentos por nombre: " + e.getMessage(), e);
        }
    }

    // Convertir Entity a DTO
    private InstrumentoDTO convertToDTO(Instrumento instrumento) {
        if (instrumento == null) {
            return null;
        }
        
        InstrumentoDTO dto = new InstrumentoDTO();
        dto.setId(instrumento.getId());
        dto.setInstrumento(instrumento.getInstrumento());
        dto.setMarca(instrumento.getMarca());
        dto.setModelo(instrumento.getModelo());
        dto.setImagen(instrumento.getImagen());
        dto.setPrecio(instrumento.getPrecio());
        dto.setCostoEnvio(instrumento.getCostoEnvio());
        dto.setCantidadVendida(instrumento.getCantidadVendida());
        dto.setDescripcion(instrumento.getDescripcion());
        
        // Información de categoría
        if (instrumento.getCategoria() != null) {
            dto.setIdCategoria(instrumento.getCategoria().getId());
            dto.setCategoriaDenominacion(instrumento.getCategoria().getDenominacion());
        }
        
        return dto;
    }
}
