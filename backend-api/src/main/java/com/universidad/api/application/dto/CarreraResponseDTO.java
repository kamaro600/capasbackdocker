package com.universidad.api.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para Carrera.
 * Sigue el principio SRP - solo maneja la transferencia de datos de carrera.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta de Carrera")
public class CarreraResponseDTO {
    
    @Schema(description = "ID único de la carrera", example = "1")
    private Long carreraId;
    
    @Schema(description = "Nombre de la carrera", example = "Ingeniería de Sistemas")
    private String nombre;
    
    @Schema(description = "Descripción detallada de la carrera")
    private String descripcion;
    
    @Schema(description = "Duración en semestres", example = "10")
    private Integer duracionSemestres;
    
    @Schema(description = "Título que otorga la carrera", example = "Ingeniero de Sistemas")
    private String tituloOtorgado;
    
    @Schema(description = "Fecha de registro de la carrera")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaRegistro;
    
    @Schema(description = "Estado activo de la carrera", example = "true")
    private Boolean activo;
    
    @Schema(description = "ID de la facultad asociada", example = "1")
    private Long facultadId;
    
    @Schema(description = "Nombre de la facultad asociada", example = "Facultad de Ingeniería")
    private String nombreFacultad;
}