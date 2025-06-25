package com.instrumentos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InstrumentosSandovalApplication {

    public static void main(String[] args) {
        SpringApplication.run(InstrumentosSandovalApplication.class, args);
        System.out.println("🚀 Servidor Spring Boot iniciado en http://localhost:3001");
        System.out.println("📊 API disponible en http://localhost:3001/api/instrumentos");
        System.out.println("🖼️ Imágenes disponibles en http://localhost:3001/images/");
    }
}
