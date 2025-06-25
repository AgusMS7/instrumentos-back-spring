package com.instrumentos.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.instrumentos.dto.ApiResponse;
import com.instrumentos.dto.ProductImageDTO;
import com.instrumentos.service.InstrumentoService;
import com.instrumentos.service.ProductImageService;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(originPatterns = {"http://localhost:*", "https://localhost:*"}, allowCredentials = "true")
public class ImageController {
    
    private static final String UPLOAD_DIR = "public/images/";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png", ".webp", ".gif"};
    
    @Autowired
    private ProductImageService productImageService;
    
    @Autowired
    private InstrumentoService instrumentoService;
    
    // Subir imagen para un instrumento
    @PostMapping("/upload/{instrumentoId}")
    public ResponseEntity<ApiResponse<ProductImageDTO>> uploadImage(
            @PathVariable Long instrumentoId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "false") boolean isPrimary,
            @RequestParam(required = false) String altText) {
        
        try {
            // Validar que el instrumento existe
            if (!instrumentoService.existsById(instrumentoId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Instrumento no encontrado", null));
            }
            
            // Validar archivo
            String validationError = validateFile(file);
            if (validationError != null) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, validationError, null));
            }
            
            // Crear directorio si no existe
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // Generar nombre único para el archivo
            String originalFilename = file.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            String uniqueFilename = UUID.randomUUID().toString() + extension;
            
            // Guardar archivo
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // Crear registro en base de datos
            ProductImageDTO imageDTO = productImageService.createImage(
                instrumentoId, 
                uniqueFilename, 
                altText != null ? altText : originalFilename,
                isPrimary
            );
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Imagen subida exitosamente", imageDTO));
                
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Error al guardar la imagen: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Error interno: " + e.getMessage(), null));
        }
    }
    
    // Obtener imagen por nombre de archivo
    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path imagePath = Paths.get(UPLOAD_DIR).resolve(filename);
            
            if (!Files.exists(imagePath) || !Files.isReadable(imagePath)) {
                return ResponseEntity.notFound().build();
            }
            
            Resource resource = new FileSystemResource(imagePath.toFile());
            String contentType = determineContentType(filename);
            
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .header(HttpHeaders.CACHE_CONTROL, "max-age=3600")
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
                
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Obtener todas las imágenes de un instrumento
    @GetMapping("/instrumento/{instrumentoId}")
    public ResponseEntity<ApiResponse<List<ProductImageDTO>>> getImagesByInstrumento(@PathVariable Long instrumentoId) {
        try {
            List<ProductImageDTO> images = productImageService.getImagesByInstrumento(instrumentoId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Imágenes obtenidas exitosamente", images));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Error al obtener imágenes: " + e.getMessage(), null));
        }
    }
    
    // Obtener imagen principal de un instrumento
    @GetMapping("/instrumento/{instrumentoId}/primary")
    public ResponseEntity<ApiResponse<ProductImageDTO>> getPrimaryImage(@PathVariable Long instrumentoId) {
        try {
            ProductImageDTO primaryImage = productImageService.getPrimaryImage(instrumentoId);
            if (primaryImage != null) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Imagen principal encontrada", primaryImage));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "No se encontró imagen principal", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Error al obtener imagen principal: " + e.getMessage(), null));
        }
    }
    
    // Establecer imagen como principal
    @PostMapping("/{imageId}/set-primary")
    public ResponseEntity<ApiResponse<ProductImageDTO>> setPrimaryImage(@PathVariable Long imageId) {
        try {
            ProductImageDTO updatedImage = productImageService.setPrimaryImage(imageId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Imagen establecida como principal", updatedImage));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Error al establecer imagen principal: " + e.getMessage(), null));
        }
    }
    
    // Eliminar imagen
    @DeleteMapping("/{imageId}")
    public ResponseEntity<ApiResponse<Void>> deleteImage(@PathVariable Long imageId) {
        try {
            productImageService.deleteImage(imageId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Imagen eliminada exitosamente", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Error al eliminar imagen: " + e.getMessage(), null));
        }
    }
    
    // Métodos auxiliares
    private String validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            return "El archivo está vacío";
        }
        
        if (file.getSize() > MAX_FILE_SIZE) {
            return "El archivo es demasiado grande. Máximo permitido: 5MB";
        }
        
        String filename = file.getOriginalFilename();
        if (filename == null) {
            return "Nombre de archivo inválido";
        }
        
        String extension = getFileExtension(filename).toLowerCase();
        boolean validExtension = false;
        for (String allowedExt : ALLOWED_EXTENSIONS) {
            if (extension.equals(allowedExt)) {
                validExtension = true;
                break;
            }
        }
        
        if (!validExtension) {
            return "Tipo de archivo no permitido. Permitidos: " + String.join(", ", ALLOWED_EXTENSIONS);
        }
        
        return null; // Sin errores
    }
    
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf('.') == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.'));
    }
    
    private String determineContentType(String filename) {
        String extension = getFileExtension(filename).toLowerCase();
        switch (extension) {
            case ".png": return "image/png";
            case ".gif": return "image/gif";
            case ".webp": return "image/webp";
            case ".jpg":
            case ".jpeg":
            default: return "image/jpeg";
        }
    }
}
