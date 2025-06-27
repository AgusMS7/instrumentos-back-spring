package com.instrumentos.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.PutMapping;
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
   
   private static final Logger logger = LoggerFactory.getLogger(ImageController.class);
   private static final String UPLOAD_DIR = "public/images/";
   private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
   private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png", ".webp", ".gif"};
   
   @Autowired
   private ProductImageService productImageService;
   
   @Autowired
   private InstrumentoService instrumentoService;
   
   @PostMapping("/upload/{instrumentoId}")
   public ResponseEntity<ApiResponse<ProductImageDTO>> uploadImage(
           @PathVariable Long instrumentoId,
           @RequestParam("file") MultipartFile file,
           @RequestParam(defaultValue = "false") boolean isPrimary,
           @RequestParam(required = false) String altText) {
       
       try {
           logger.info("Subiendo imagen para instrumento ID: {}", instrumentoId);
           
           if (!instrumentoService.existsById(instrumentoId)) {
               return ResponseEntity.status(HttpStatus.NOT_FOUND)
                   .body(new ApiResponse<>(false, "Instrumento no encontrado", null));
           }
           
           String validationError = validateFile(file);
           if (validationError != null) {
               return ResponseEntity.badRequest()
                   .body(new ApiResponse<>(false, validationError, null));
           }
           
           Path uploadPath = Paths.get(UPLOAD_DIR);
           if (!Files.exists(uploadPath)) {
               Files.createDirectories(uploadPath);
           }
           
           String originalFilename = file.getOriginalFilename();
           String extension = getFileExtension(originalFilename);
           String uniqueFilename = UUID.randomUUID().toString() + extension;
           
           Path filePath = uploadPath.resolve(uniqueFilename);
           Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
           
           ProductImageDTO imageDTO = productImageService.createImage(
               instrumentoId, 
               uniqueFilename, 
               altText != null ? altText : originalFilename,
               isPrimary
           );
           
           logger.info("Imagen subida exitosamente: {}", uniqueFilename);
           return ResponseEntity.status(HttpStatus.CREATED)
               .body(new ApiResponse<>(true, "Imagen subida exitosamente", imageDTO));
               
       } catch (IOException e) {
           logger.error("Error de E/S al subir imagen: {}", e.getMessage(), e);
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
               .body(new ApiResponse<>(false, "Error al guardar la imagen: " + e.getMessage(), null));
       } catch (Exception e) {
           logger.error("Error al subir imagen: {}", e.getMessage(), e);
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
               .body(new ApiResponse<>(false, "Error interno: " + e.getMessage(), null));
       }
   }
   
   @GetMapping("/{filename:.+}")
   public ResponseEntity<Resource> getImage(@PathVariable String filename) {
       try {
           Path imagePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
           
           if (!Files.exists(imagePath) || !Files.isReadable(imagePath)) {
               logger.warn("Imagen no encontrada o no legible: {}", filename);
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
           logger.error("Error al obtener imagen {}: {}", filename, e.getMessage(), e);
           return ResponseEntity.notFound().build();
       }
   }
   
   @GetMapping("/instrumento/{instrumentoId}")
   public ResponseEntity<ApiResponse<List<ProductImageDTO>>> getImagesByInstrumento(@PathVariable Long instrumentoId) {
       try {
           List<ProductImageDTO> images = productImageService.getImagesByInstrumento(instrumentoId);
           return ResponseEntity.ok(new ApiResponse<>(true, "Imágenes obtenidas exitosamente", images));
       } catch (Exception e) {
           logger.error("Error al obtener imágenes del instrumento {}: {}", instrumentoId, e.getMessage(), e);
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
               .body(new ApiResponse<>(false, "Error al obtener imágenes: " + e.getMessage(), null));
       }
   }
   
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
           logger.error("Error al obtener imagen principal del instrumento {}: {}", instrumentoId, e.getMessage(), e);
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
               .body(new ApiResponse<>(false, "Error al obtener imagen principal: " + e.getMessage(), null));
       }
   }
   
   @PutMapping("/{imageId}/primary")
   public ResponseEntity<ApiResponse<ProductImageDTO>> setPrimaryImage(@PathVariable Long imageId) {
       try {
           ProductImageDTO updatedImage = productImageService.setPrimaryImage(imageId);
           return ResponseEntity.ok(new ApiResponse<>(true, "Imagen establecida como principal", updatedImage));
       } catch (RuntimeException e) {
           logger.warn("Error al establecer imagen principal {}: {}", imageId, e.getMessage());
           return ResponseEntity.status(HttpStatus.NOT_FOUND)
               .body(new ApiResponse<>(false, e.getMessage(), null));
       } catch (Exception e) {
           logger.error("Error al establecer imagen principal {}: {}", imageId, e.getMessage(), e);
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
               .body(new ApiResponse<>(false, "Error al establecer imagen principal: " + e.getMessage(), null));
       }
   }
   
   @DeleteMapping("/{imageId}")
   public ResponseEntity<ApiResponse<Void>> deleteImage(@PathVariable Long imageId) {
       try {
           productImageService.deleteImage(imageId);
           return ResponseEntity.ok(new ApiResponse<>(true, "Imagen eliminada exitosamente", null));
       } catch (RuntimeException e) {
           logger.warn("Error al eliminar imagen {}: {}", imageId, e.getMessage());
           return ResponseEntity.status(HttpStatus.NOT_FOUND)
               .body(new ApiResponse<>(false, e.getMessage(), null));
       } catch (Exception e) {
           logger.error("Error al eliminar imagen {}: {}", imageId, e.getMessage(), e);
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
               .body(new ApiResponse<>(false, "Error al eliminar imagen: " + e.getMessage(), null));
       }
   }
   
   private String validateFile(MultipartFile file) {
       if (file.isEmpty()) {
           return "El archivo está vacío";
       }
       
       if (file.getSize() > MAX_FILE_SIZE) {
           return "El archivo es demasiado grande. Máximo permitido: 5MB";
       }
       
       String filename = file.getOriginalFilename();
       if (filename == null || filename.trim().isEmpty()) {
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
       
       return null;
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
