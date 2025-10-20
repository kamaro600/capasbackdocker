package com.universidad.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Clase principal de la aplicación Spring Boot.
 * 
 * Esta aplicación sigue los principios SOLID:
 * - Arquitectura limpia con separación de capas
 * - Inyección de dependencias para inversión de control
 * - Interfaces para abstracciones
 * - Responsabilidades específicas por capa
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.universidad.api.domain.repositories")
public class UniversidadApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniversidadApiApplication.class, args);
    }
}