package com.universidad.api.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de respuesta para Facultad - Patrón MVC.
 * DATA TRANSFER OBJECT: Transporta datos desde el Model hacia la View
 * - Define la estructura de datos que retorna la API
 * - Separa la representación externa de la entidad interna
 * - Controla qué información se expone al cliente
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta de Facultad")
public class FacultadResponseDTO {
    
    @Schema(description = "ID único de la facultad", example = "1")
    private Long facultadId;
    
    @Schema(description = "Nombre de la facultad", example = "Facultad de Ingeniería")
    private String nombre;
    
    @Schema(description = "Descripción detallada de la facultad")
    private String descripcion;
    
    @Schema(description = "Ubicación física de la facultad", example = "Edificio Central, Piso 3")
    private String ubicacion;
    
    @Schema(description = "Nombre del decano actual", example = "Dr. Juan Pérez")
    private String decano;
    
    @Schema(description = "Fecha de registro de la facultad")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaRegistro;
    
    @Schema(description = "Estado activo de la facultad", example = "true")
    private Boolean activo;
    
    @Schema(description = "Lista de carreras asociadas a la facultad")
    private List<CarreraResponseDTO> carreras;
}