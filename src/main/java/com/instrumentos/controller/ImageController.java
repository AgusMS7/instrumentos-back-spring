package com.instrumentos.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/images")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class ImageController {
    
    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Resource resource = null;
            
            System.out.println("🔍 Buscando imagen: " + filename);
            
            // 1. Buscar en public/images del proyecto backend
            Path publicPath = Paths.get("public", "images", filename);
            System.out.println("📁 Buscando en: " + publicPath.toAbsolutePath());
            if (Files.exists(publicPath) && Files.isReadable(publicPath)) {
                resource = new FileSystemResource(publicPath.toFile());
                System.out.println("✅ Imagen encontrada en: " + publicPath.toAbsolutePath());
            }
            
            // 2. Buscar en src/main/resources/static/images del backend
            if (resource == null || !resource.exists()) {
                Path resourcePath = Paths.get("src", "main", "resources", "static", "images", filename);
                System.out.println("📁 Buscando en: " + resourcePath.toAbsolutePath());
                if (Files.exists(resourcePath) && Files.isReadable(resourcePath)) {
                    resource = new FileSystemResource(resourcePath.toFile());
                    System.out.println("✅ Imagen encontrada en: " + resourcePath.toAbsolutePath());
                }
            }
            
            // 3. Buscar en classpath (resources compilados)
            if (resource == null || !resource.exists()) {
                try {
                    resource = new ClassPathResource("static/images/" + filename);
                    if (resource.exists()) {
                        System.out.println("✅ Imagen encontrada en classpath: static/images/" + filename);
                    } else {
                        System.out.println("❌ No encontrada en classpath: static/images/" + filename);
                    }
                } catch (Exception e) {
                    System.out.println("❌ Error en classpath: " + e.getMessage());
                }
            }
            
            // 4. Buscar en target/classes/static/images (compilado)
            if (resource == null || !resource.exists()) {
                Path targetPath = Paths.get("target", "classes", "static", "images", filename);
                System.out.println("📁 Buscando en: " + targetPath.toAbsolutePath());
                if (Files.exists(targetPath) && Files.isReadable(targetPath)) {
                    resource = new FileSystemResource(targetPath.toFile());
                    System.out.println("✅ Imagen encontrada en: " + targetPath.toAbsolutePath());
                }
            }
            
            if (resource != null && resource.exists()) {
                // Determinar el tipo de contenido
                String contentType = "image/jpeg";
                if (filename.toLowerCase().endsWith(".png")) {
                    contentType = "image/png";
                } else if (filename.toLowerCase().endsWith(".gif")) {
                    contentType = "image/gif";
                } else if (filename.toLowerCase().endsWith(".webp")) {
                    contentType = "image/webp";
                }
                
                System.out.println("✅ Sirviendo imagen: " + filename + " (" + contentType + ")");
                
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                        .header(HttpHeaders.CACHE_CONTROL, "max-age=3600")
                        .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            } else {
                System.out.println("❌ Imagen NO encontrada: " + filename);
                System.out.println("📍 Directorio actual: " + System.getProperty("user.dir"));
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("❌ Error sirviendo archivo " + filename + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
    
    // Endpoint de debug para listar imágenes disponibles
    @GetMapping("/debug/list")
    public ResponseEntity<String> listImages() {
        StringBuilder sb = new StringBuilder();
        sb.append("🔍 Debug de imágenes - Directorio actual: ").append(System.getProperty("user.dir")).append("\n\n");
        
        // Verificar public/images
        sb.append("📁 public/images/:\n");
        Path publicDir = Paths.get("public", "images");
        sb.append("   Ruta: ").append(publicDir.toAbsolutePath()).append("\n");
        if (Files.exists(publicDir)) {
            try {
                Files.list(publicDir).forEach(path -> {
                    sb.append("  ✅ ").append(path.getFileName()).append("\n");
                });
            } catch (Exception e) {
                sb.append("  ❌ Error listando: ").append(e.getMessage()).append("\n");
            }
        } else {
            sb.append("  ❌ Directorio no existe\n");
        }
        
        // Verificar src/main/resources/static/images
        sb.append("\n📁 src/main/resources/static/images/:\n");
        Path resourceDir = Paths.get("src", "main", "resources", "static", "images");
        sb.append("   Ruta: ").append(resourceDir.toAbsolutePath()).append("\n");
        if (Files.exists(resourceDir)) {
            try {
                Files.list(resourceDir).forEach(path -> {
                    sb.append("  ✅ ").append(path.getFileName()).append("\n");
                });
            } catch (Exception e) {
                sb.append("  ❌ Error listando: ").append(e.getMessage()).append("\n");
            }
        } else {
            sb.append("  ❌ Directorio no existe\n");
        }
        
        // Verificar target/classes/static/images
        sb.append("\n📁 target/classes/static/images/:\n");
        Path targetDir = Paths.get("target", "classes", "static", "images");
        sb.append("   Ruta: ").append(targetDir.toAbsolutePath()).append("\n");
        if (Files.exists(targetDir)) {
            try {
                Files.list(targetDir).forEach(path -> {
                    sb.append("  ✅ ").append(path.getFileName()).append("\n");
                });
            } catch (Exception e) {
                sb.append("  ❌ Error listando: ").append(e.getMessage()).append("\n");
            }
        } else {
            sb.append("  ❌ Directorio no existe\n");
        }
        
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(sb.toString());
    }
}
