package com.universidad.api.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración web general de la aplicación.
 * Sigue el principio SRP - configuración específica para aspectos web.
 */
@Slf4j
@Configuration
public class WebConfig {
    // Configuración web básica sin CORS (manejado por CorsConfig)
}