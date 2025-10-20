package com.universidad.api.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de solicitud para crear/actualizar Carrera.
 * Sigue el principio SRP - solo maneja validación y transferencia de datos de entrada.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Solicitud para crear o actualizar Carrera")
public class CarreraRequestDTO {
    
    @NotBlank(message = "El nombre de la carrera es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Schema(description = "Nombre de la carrera", example = "Ingeniería de Sistemas", required = true)
    private String nombre;
    
    @Schema(description = "Descripción detallada de la carrera")
    private String descripcion;
    
    @NotNull(message = "La duración en semestres es obligatoria")
    @Min(value = 1, message = "La duración debe ser al menos 1 semestre")
    @Schema(description = "Duración en semestres", example = "10", required = true)
    private Integer duracionSemestres;
    
    @Size(max = 100, message = "El título otorgado no puede exceder 100 caracteres")
    @Schema(description = "Título que otorga la carrera", example = "Ingeniero de Sistemas")
    private String tituloOtorgado;
    
    @Schema(description = "Estado activo de la carrera", example = "true")
    private Boolean activo;
    
    @NotNull(message = "El ID de la facultad es obligatorio")
    @Schema(description = "ID de la facultad asociada", example = "1", required = true)
    private Long facultadId;
}