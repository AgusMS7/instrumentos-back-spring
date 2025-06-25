package com.instrumentos.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    
    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "API de Instrumentos Sandoval funcionando");
        response.put("version", "1.0.0");
        response.put("framework", "Spring Boot");
        response.put("language", "Java");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("instrumentos", "/api/instrumentos");
        endpoints.put("instrumento_por_id", "/api/instrumentos/{id}");
        endpoints.put("crear_instrumento", "POST /api/instrumentos");
        endpoints.put("actualizar_instrumento", "PUT /api/instrumentos/{id}");
        endpoints.put("eliminar_instrumento", "DELETE /api/instrumentos/{id}");
        endpoints.put("buscar", "/api/instrumentos/search");
        endpoints.put("envio_gratis", "/api/instrumentos/free-shipping");
        endpoints.put("images", "/images/{filename}");
        
        response.put("endpoints", endpoints);
        
        return response;
    }
}
