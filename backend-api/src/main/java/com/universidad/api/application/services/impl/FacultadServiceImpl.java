package com.universidad.api.application.services.impl;

import com.universidad.api.application.dto.FacultadRequestDTO;
import com.universidad.api.application.dto.FacultadResponseDTO;
import com.universidad.api.application.mappers.FacultadMapper;
import com.universidad.api.application.services.FacultadService;
import com.universidad.api.domain.entities.Facultad;
import com.universidad.api.domain.repositories.FacultadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

/**
 * Implementación del servicio de Facultad.
 * Sigue los principios SOLID:
 * - SRP: Responsabilidad única de gestionar facultades
 * - OCP: Abierto para extensión a través de la interfaz
 * - LSP: Implementa correctamente la interfaz FacultadService
 * - ISP: Interfaz segregada específica para facultades
 * - DIP: Depende de abstracciones (FacultadRepository, FacultadMapper)
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FacultadServiceImpl implements FacultadService {
    
    private final FacultadRepository facultadRepository;
    private final FacultadMapper facultadMapper;
    
    @Override
    public FacultadResponseDTO crear(FacultadRequestDTO requestDTO) {
        log.info("Creando nueva facultad: {}", requestDTO.getNombre());
        
        // Validar que no exista una facultad con el mismo nombre
        if (facultadRepository.existsByNombreIgnoreCase(requestDTO.getNombre())) {
            throw new IllegalArgumentException(
                "Ya existe una facultad con el nombre: " + requestDTO.getNombre()
            );
        }
        
        Facultad facultad = facultadMapper.toEntity(requestDTO);
        Facultad facultadGuardada = facultadRepository.save(facultad);
        
        log.info("Facultad creada exitosamente con ID: {}", facultadGuardada.getFacultadId());
        return facultadMapper.toResponseDTO(facultadGuardada);
    }
    
    @Override
    @Transactional(readOnly = true)
    public FacultadResponseDTO obtenerPorId(Long id) {
        log.info("Obteniendo facultad por ID: {}", id);
        
        Facultad facultad = facultadRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(
                "No se encontró facultad con ID: " + id
            ));
            
        return facultadMapper.toResponseDTO(facultad);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<FacultadResponseDTO> obtenerTodas() {
        log.info("Obteniendo todas las facultades");
        
        List<Facultad> facultades = facultadRepository.findAll();
        return facultadMapper.toResponseDTOList(facultades);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<FacultadResponseDTO> obtenerActivas() {
        log.info("Obteniendo facultades activas");
        
        List<Facultad> facultades = facultadRepository.findByActivoTrue();
        return facultadMapper.toResponseDTOList(facultades);
    }
    
    @Override
    public FacultadResponseDTO actualizar(Long id, FacultadRequestDTO requestDTO) {
        log.info("Actualizando facultad ID: {} con datos: {}", id, requestDTO.getNombre());
        
        Facultad facultadExistente = facultadRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(
                "No se encontró facultad con ID: " + id
            ));
        
        // Validar que no exista otra facultad con el mismo nombre
        if (facultadRepository.existsByNombreIgnoreCaseAndFacultadIdNot(
            requestDTO.getNombre(), id)) {
            throw new IllegalArgumentException(
                "Ya existe otra facultad con el nombre: " + requestDTO.getNombre()
            );
        }
        
        facultadMapper.updateEntityFromDTO(requestDTO, facultadExistente);
        Facultad facultadActualizada = facultadRepository.save(facultadExistente);
        
        log.info("Facultad actualizada exitosamente: {}", facultadActualizada.getFacultadId());
        return facultadMapper.toResponseDTO(facultadActualizada);
    }
    
    @Override
    public void eliminar(Long id) {
        log.info("Eliminando facultad ID: {}", id);
        
        Facultad facultad = facultadRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(
                "No se encontró facultad con ID: " + id
            ));
        
        // Eliminación lógica
        facultad.setActivo(false);
        facultadRepository.save(facultad);
        
        log.info("Facultad eliminada (desactivada) exitosamente: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public FacultadResponseDTO buscarPorNombre(String nombre) {
        log.info("Buscando facultad por nombre: {}", nombre);
        
        Facultad facultad = facultadRepository.findByNombreIgnoreCase(nombre)
            .orElseThrow(() -> new EntityNotFoundException(
                "No se encontró facultad con nombre: " + nombre
            ));
            
        return facultadMapper.toResponseDTO(facultad);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<FacultadResponseDTO> buscarPorDecano(String decano) {
        log.info("Buscando facultades por decano: {}", decano);
        
        List<Facultad> facultades = facultadRepository.findByDecanoContainingIgnoreCase(decano);
        return facultadMapper.toResponseDTOList(facultades);
    }
}