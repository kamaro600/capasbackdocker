package com.universidad.api.infrastructure.web.controllers;

import com.universidad.api.application.dto.CarreraRequestDTO;
import com.universidad.api.application.dto.CarreraResponseDTO;
import com.universidad.api.application.services.CarreraService;
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
 * Controlador REST para Carreras.
 * Sigue los principios SOLID:
 * - SRP: Solo maneja operaciones HTTP para carreras
 * - OCP: Extensible para nuevas operaciones
 * - DIP: Depende de la abstracción CarreraService
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/carreras")
@RequiredArgsConstructor
@Tag(name = "Carreras", description = "Operaciones CRUD para gestión de carreras")
public class CarreraController {
    
    private final CarreraService carreraService;
    
    @Operation(summary = "Crear nueva carrera", description = "Crea una nueva carrera en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Carrera creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Facultad no encontrada"),
        @ApiResponse(responseCode = "409", description = "Ya existe una carrera con ese nombre")
    })
    @PostMapping
    public ResponseEntity<CarreraResponseDTO> crear(
            @Parameter(description = "Datos de la carrera a crear")
            @Valid @RequestBody CarreraRequestDTO requestDTO) {
        
        log.info("REST: Creando nueva carrera: {}", requestDTO.getNombre());
        CarreraResponseDTO response = carreraService.crear(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @Operation(summary = "Obtener carrera por ID", description = "Obtiene una carrera específica por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Carrera encontrada"),
        @ApiResponse(responseCode = "404", description = "Carrera no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CarreraResponseDTO> obtenerPorId(
            @Parameter(description = "ID de la carrera")
            @PathVariable Long id) {
        
        log.info("REST: Obteniendo carrera por ID: {}", id);
        CarreraResponseDTO response = carreraService.obtenerPorId(id);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Listar todas las carreras", description = "Obtiene todas las carreras del sistema")
    @ApiResponse(responseCode = "200", description = "Lista de carreras obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<CarreraResponseDTO>> obtenerTodas(
            @Parameter(description = "Filtrar solo carreras activas")
            @RequestParam(required = false, defaultValue = "false") boolean soloActivas) {
        
        log.info("REST: Obteniendo todas las carreras (soloActivas: {})", soloActivas);
        
        List<CarreraResponseDTO> response = soloActivas 
            ? carreraService.obtenerActivas()
            : carreraService.obtenerTodas();
            
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Listar carreras por facultad", description = "Obtiene todas las carreras de una facultad específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de carreras obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Facultad no encontrada")
    })
    @GetMapping("/facultad/{facultadId}")
    public ResponseEntity<List<CarreraResponseDTO>> obtenerPorFacultad(
            @Parameter(description = "ID de la facultad")
            @PathVariable Long facultadId,
            @Parameter(description = "Filtrar solo carreras activas")
            @RequestParam(required = false, defaultValue = "false") boolean soloActivas) {
        
        log.info("REST: Obteniendo carreras de facultad ID: {} (soloActivas: {})", 
                facultadId, soloActivas);
        
        List<CarreraResponseDTO> response = soloActivas 
            ? carreraService.obtenerActivasPorFacultad(facultadId)
            : carreraService.obtenerPorFacultad(facultadId);
            
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Actualizar carrera", description = "Actualiza una carrera existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Carrera actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Carrera no encontrada"),
        @ApiResponse(responseCode = "409", description = "Ya existe otra carrera con ese nombre")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CarreraResponseDTO> actualizar(
            @Parameter(description = "ID de la carrera a actualizar")
            @PathVariable Long id,
            @Parameter(description = "Nuevos datos de la carrera")
            @Valid @RequestBody CarreraRequestDTO requestDTO) {
        
        log.info("REST: Actualizando carrera ID: {}", id);
        CarreraResponseDTO response = carreraService.actualizar(id, requestDTO);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Eliminar carrera", description = "Elimina lógicamente una carrera (la marca como inactiva)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Carrera eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Carrera no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la carrera a eliminar")
            @PathVariable Long id) {
        
        log.info("REST: Eliminando carrera ID: {}", id);
        carreraService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary = "Buscar carrera por nombre", description = "Busca una carrera por su nombre exacto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Carrera encontrada"),
        @ApiResponse(responseCode = "404", description = "No se encontró carrera con ese nombre")
    })
    @GetMapping("/buscar/nombre/{nombre}")
    public ResponseEntity<CarreraResponseDTO> buscarPorNombre(
            @Parameter(description = "Nombre de la carrera a buscar")
            @PathVariable String nombre) {
        
        log.info("REST: Buscando carrera por nombre: {}", nombre);
        CarreraResponseDTO response = carreraService.buscarPorNombre(nombre);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Buscar carreras por duración", description = "Busca carreras por su duración en semestres")
    @ApiResponse(responseCode = "200", description = "Lista de carreras encontradas")
    @GetMapping("/buscar/duracion/{duracion}")
    public ResponseEntity<List<CarreraResponseDTO>> buscarPorDuracion(
            @Parameter(description = "Duración en semestres")
            @PathVariable Integer duracion) {
        
        log.info("REST: Buscando carreras por duración: {} semestres", duracion);
        List<CarreraResponseDTO> response = carreraService.buscarPorDuracion(duracion);
        return ResponseEntity.ok(response);
    }
}