package com.universidad.api.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de solicitud para crear/actualizar Facultad.
 * Sigue el principio SRP - solo maneja validación y transferencia de datos de entrada.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Solicitud para crear o actualizar Facultad")
public class FacultadRequestDTO {
    
    @NotBlank(message = "El nombre de la facultad es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Schema(description = "Nombre de la facultad", example = "Facultad de Ingeniería", required = true)
    private String nombre;
    
    @Schema(description = "Descripción detallada de la facultad")
    private String descripcion;
    
    @Size(max = 100, message = "La ubicación no puede exceder 100 caracteres")
    @Schema(description = "Ubicación física de la facultad", example = "Edificio Central, Piso 3")
    private String ubicacion;
    
    @Size(max = 100, message = "El nombre del decano no puede exceder 100 caracteres")
    @Schema(description = "Nombre del decano actual", example = "Dr. Juan Pérez")
    private String decano;
    
    @Schema(description = "Estado activo de la facultad", example = "true")
    private Boolean activo;
}