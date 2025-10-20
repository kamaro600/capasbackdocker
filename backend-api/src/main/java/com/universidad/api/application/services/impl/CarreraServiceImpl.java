package com.universidad.api.application.services.impl;

import com.universidad.api.application.dto.CarreraRequestDTO;
import com.universidad.api.application.dto.CarreraResponseDTO;
import com.universidad.api.application.mappers.CarreraMapper;
import com.universidad.api.application.services.CarreraService;
import com.universidad.api.domain.entities.Carrera;
import com.universidad.api.domain.entities.Facultad;
import com.universidad.api.domain.repositories.CarreraRepository;
import com.universidad.api.domain.repositories.FacultadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

/**
 * Implementación del servicio de Carrera.
 * Sigue los principios SOLID:
 * - SRP: Responsabilidad única de gestionar carreras
 * - OCP: Abierto para extensión a través de la interfaz
 * - LSP: Implementa correctamente la interfaz CarreraService
 * - ISP: Interfaz segregada específica para carreras
 * - DIP: Depende de abstracciones (CarreraRepository, FacultadRepository, CarreraMapper)
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CarreraServiceImpl implements CarreraService {
    
    private final CarreraRepository carreraRepository;
    private final FacultadRepository facultadRepository;
    private final CarreraMapper carreraMapper;
    
    @Override
    public CarreraResponseDTO crear(CarreraRequestDTO requestDTO) {
        log.info("Creando nueva carrera: {} para facultad ID: {}", 
                requestDTO.getNombre(), requestDTO.getFacultadId());
        
        // Validar que no exista una carrera con el mismo nombre
        if (carreraRepository.existsByNombreIgnoreCase(requestDTO.getNombre())) {
            throw new IllegalArgumentException(
                "Ya existe una carrera con el nombre: " + requestDTO.getNombre()
            );
        }
        
        // Validar que exista la facultad
        Facultad facultad = facultadRepository.findById(requestDTO.getFacultadId())
            .orElseThrow(() -> new EntityNotFoundException(
                "No se encontró facultad con ID: " + requestDTO.getFacultadId()
            ));
        
        // Validar que la facultad esté activa
        if (!facultad.getActivo()) {
            throw new IllegalArgumentException(
                "No se puede crear una carrera en una facultad inactiva"
            );
        }
        
        Carrera carrera = carreraMapper.toEntity(requestDTO);
        carrera.setFacultad(facultad);
        
        Carrera carreraGuardada = carreraRepository.save(carrera);
        
        log.info("Carrera creada exitosamente con ID: {}", carreraGuardada.getCarreraId());
        return carreraMapper.toResponseDTO(carreraGuardada);
    }
    
    @Override
    @Transactional(readOnly = true)
    public CarreraResponseDTO obtenerPorId(Long id) {
        log.info("Obteniendo carrera por ID: {}", id);
        
        Carrera carrera = carreraRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(
                "No se encontró carrera con ID: " + id
            ));
            
        return carreraMapper.toResponseDTO(carrera);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CarreraResponseDTO> obtenerTodas() {
        log.info("Obteniendo todas las carreras");
        
        List<Carrera> carreras = carreraRepository.findAll();
        return carreraMapper.toResponseDTOList(carreras);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CarreraResponseDTO> obtenerActivas() {
        log.info("Obteniendo carreras activas");
        
        List<Carrera> carreras = carreraRepository.findByActivoTrue();
        return carreraMapper.toResponseDTOList(carreras);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CarreraResponseDTO> obtenerPorFacultad(Long facultadId) {
        log.info("Obteniendo carreras de facultad ID: {}", facultadId);
        
        // Validar que exista la facultad
        if (!facultadRepository.existsById(facultadId)) {
            throw new EntityNotFoundException(
                "No se encontró facultad con ID: " + facultadId
            );
        }
        
        List<Carrera> carreras = carreraRepository.findByFacultad_FacultadId(facultadId);
        return carreraMapper.toResponseDTOList(carreras);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CarreraResponseDTO> obtenerActivasPorFacultad(Long facultadId) {
        log.info("Obteniendo carreras activas de facultad ID: {}", facultadId);
        
        // Validar que exista la facultad
        if (!facultadRepository.existsById(facultadId)) {
            throw new EntityNotFoundException(
                "No se encontró facultad con ID: " + facultadId
            );
        }
        
        List<Carrera> carreras = carreraRepository.findByFacultad_FacultadIdAndActivoTrue(facultadId);
        return carreraMapper.toResponseDTOList(carreras);
    }
    
    @Override
    public CarreraResponseDTO actualizar(Long id, CarreraRequestDTO requestDTO) {
        log.info("Actualizando carrera ID: {} con datos: {}", id, requestDTO.getNombre());
        
        Carrera carreraExistente = carreraRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(
                "No se encontró carrera con ID: " + id
            ));
        
        // Validar que no exista otra carrera con el mismo nombre
        if (carreraRepository.existsByNombreIgnoreCaseAndCarreraIdNot(
            requestDTO.getNombre(), id)) {
            throw new IllegalArgumentException(
                "Ya existe otra carrera con el nombre: " + requestDTO.getNombre()
            );
        }
        
        // Si se cambia la facultad, validar que exista y esté activa
        if (requestDTO.getFacultadId() != null && 
            !requestDTO.getFacultadId().equals(carreraExistente.getFacultadId())) {
            
            Facultad nuevaFacultad = facultadRepository.findById(requestDTO.getFacultadId())
                .orElseThrow(() -> new EntityNotFoundException(
                    "No se encontró facultad con ID: " + requestDTO.getFacultadId()
                ));
                
            if (!nuevaFacultad.getActivo()) {
                throw new IllegalArgumentException(
                    "No se puede asignar una carrera a una facultad inactiva"
                );
            }
            
            carreraExistente.setFacultad(nuevaFacultad);
        }
        
        carreraMapper.updateEntityFromDTO(requestDTO, carreraExistente);
        Carrera carreraActualizada = carreraRepository.save(carreraExistente);
        
        log.info("Carrera actualizada exitosamente: {}", carreraActualizada.getCarreraId());
        return carreraMapper.toResponseDTO(carreraActualizada);
    }
    
    @Override
    public void eliminar(Long id) {
        log.info("Eliminando carrera ID: {}", id);
        
        Carrera carrera = carreraRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(
                "No se encontró carrera con ID: " + id
            ));
        
        // Eliminación lógica
        carrera.setActivo(false);
        carreraRepository.save(carrera);
        
        log.info("Carrera eliminada (desactivada) exitosamente: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public CarreraResponseDTO buscarPorNombre(String nombre) {
        log.info("Buscando carrera por nombre: {}", nombre);
        
        Carrera carrera = carreraRepository.findByNombreIgnoreCase(nombre)
            .orElseThrow(() -> new EntityNotFoundException(
                "No se encontró carrera con nombre: " + nombre
            ));
            
        return carreraMapper.toResponseDTO(carrera);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CarreraResponseDTO> buscarPorDuracion(Integer duracion) {
        log.info("Buscando carreras por duración: {} semestres", duracion);
        
        List<Carrera> carreras = carreraRepository.findByDuracionSemestres(duracion);
        return carreraMapper.toResponseDTOList(carreras);
    }
}