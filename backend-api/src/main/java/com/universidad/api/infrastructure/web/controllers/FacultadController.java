package com.universidad.api.infrastructure.web.controllers;

import com.universidad.api.application.dto.FacultadRequestDTO;
import com.universidad.api.application.dto.FacultadResponseDTO;
import com.universidad.api.application.services.FacultadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para Facultades - Patrón MVC.
 * CONTROLLER: Maneja las peticiones HTTP y coordina entre View y Model
 * - Recibe requests HTTP
 * - Valida datos de entrada
 * - Delega lógica de negocio al Service
 * - Retorna respuestas HTTP apropiadas
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/facultades")
@RequiredArgsConstructor
@Tag(name = "Facultades", description = "Operaciones CRUD para gestión de facultades")
public class FacultadController {
    
    // Inyección de dependencia del Service (parte del Model en MVC)
    private final FacultadService facultadService;
    
    @Operation(summary = "Crear nueva facultad", description = "Crea una nueva facultad en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Facultad creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "Ya existe una facultad con ese nombre")
    })
    @PostMapping
    public ResponseEntity<FacultadResponseDTO> crear(
            @Parameter(description = "Datos de la facultad a crear")
            @Valid @RequestBody FacultadRequestDTO requestDTO) {
        
        log.info("Creando nueva facultad: {}", requestDTO.getNombre());
        FacultadResponseDTO response = facultadService.crear(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @Operation(summary = "Obtener facultad por ID", description = "Obtiene una facultad específica por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Facultad encontrada"),
        @ApiResponse(responseCode = "404", description = "Facultad no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FacultadResponseDTO> obtenerPorId(
            @Parameter(description = "ID de la facultad")
            @PathVariable Long id) {
        
        log.info("Obteniendo facultad por ID: {}", id);
        FacultadResponseDTO response = facultadService.obtenerPorId(id);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Listar todas las facultades", description = "Obtiene todas las facultades del sistema")
    @ApiResponse(responseCode = "200", description = "Lista de facultades obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<FacultadResponseDTO>> obtenerTodas(
            @Parameter(description = "Filtrar solo facultades activas")
            @RequestParam(required = false, defaultValue = "false") boolean soloActivas) {
        
        log.info("Obteniendo todas las facultades (soloActivas: {})", soloActivas);
        
        List<FacultadResponseDTO> response = soloActivas 
            ? facultadService.obtenerActivas()
            : facultadService.obtenerTodas();
            
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Actualizar facultad", description = "Actualiza una facultad existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Facultad actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Facultad no encontrada"),
        @ApiResponse(responseCode = "409", description = "Ya existe otra facultad con ese nombre")
    })
    @PutMapping("/{id}")
    public ResponseEntity<FacultadResponseDTO> actualizar(
            @Parameter(description = "ID de la facultad a actualizar")
            @PathVariable Long id,
            @Parameter(description = "Nuevos datos de la facultad")
            @Valid @RequestBody FacultadRequestDTO requestDTO) {
        
        log.info("Actualizando facultad ID: {}", id);
        FacultadResponseDTO response = facultadService.actualizar(id, requestDTO);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Eliminar facultad", description = "Elimina lógicamente una facultad (la marca como inactiva)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Facultad eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Facultad no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la facultad a eliminar")
            @PathVariable Long id) {
        
        log.info("Eliminando facultad ID: {}", id);
        facultadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary = "Buscar facultad por nombre", description = "Busca una facultad por su nombre exacto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Facultad encontrada"),
        @ApiResponse(responseCode = "404", description = "No se encontró facultad con ese nombre")
    })
    @GetMapping("/buscar/nombre/{nombre}")
    public ResponseEntity<FacultadResponseDTO> buscarPorNombre(
            @Parameter(description = "Nombre de la facultad a buscar")
            @PathVariable String nombre) {
        
        log.info("Buscando facultad por nombre: {}", nombre);
        FacultadResponseDTO response = facultadService.buscarPorNombre(nombre);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Buscar facultades por decano", description = "Busca facultades que contengan el nombre del decano")
    @ApiResponse(responseCode = "200", description = "Lista de facultades encontradas")
    @GetMapping("/buscar/decano")
    public ResponseEntity<List<FacultadResponseDTO>> buscarPorDecano(
            @Parameter(description = "Nombre del decano a buscar")
            @RequestParam String decano) {
        
        log.info("Buscando facultades por decano: {}", decano);
        List<FacultadResponseDTO> response = facultadService.buscarPorDecano(decano);
        return ResponseEntity.ok(response);
    }
}