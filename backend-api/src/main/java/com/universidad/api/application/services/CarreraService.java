package com.universidad.api.application.services;

import com.universidad.api.application.dto.CarreraRequestDTO;
import com.universidad.api.application.dto.CarreraResponseDTO;

import java.util.List;

/**
 * Interfaz del servicio de Carrera.
 * Sigue el principio ISP (Interface Segregation Principle) y DIP (Dependency Inversion Principle).
 */
public interface CarreraService {
    
    /**
     * Crea una nueva carrera.
     * @param requestDTO Datos de la carrera a crear
     * @return DTO con los datos de la carrera creada
     */
    CarreraResponseDTO crear(CarreraRequestDTO requestDTO);
    
    /**
     * Obtiene una carrera por ID.
     * @param id ID de la carrera
     * @return DTO con los datos de la carrera
     */
    CarreraResponseDTO obtenerPorId(Long id);
    
    /**
     * Obtiene todas las carreras.
     * @return Lista de DTOs con todas las carreras
     */
    List<CarreraResponseDTO> obtenerTodas();
    
    /**
     * Obtiene solo las carreras activas.
     * @return Lista de DTOs con las carreras activas
     */
    List<CarreraResponseDTO> obtenerActivas();
    
    /**
     * Obtiene carreras por facultad.
     * @param facultadId ID de la facultad
     * @return Lista de carreras de la facultad
     */
    List<CarreraResponseDTO> obtenerPorFacultad(Long facultadId);
    
    /**
     * Obtiene carreras activas por facultad.
     * @param facultadId ID de la facultad
     * @return Lista de carreras activas de la facultad
     */
    List<CarreraResponseDTO> obtenerActivasPorFacultad(Long facultadId);
    
    /**
     * Actualiza una carrera existente.
     * @param id ID de la carrera a actualizar
     * @param requestDTO Nuevos datos de la carrera
     * @return DTO con los datos de la carrera actualizada
     */
    CarreraResponseDTO actualizar(Long id, CarreraRequestDTO requestDTO);
    
    /**
     * Elimina lógicamente una carrera (marca como inactiva).
     * @param id ID de la carrera a eliminar
     */
    void eliminar(Long id);
    
    /**
     * Busca una carrera por nombre.
     * @param nombre Nombre a buscar
     * @return DTO con la carrera encontrada si existe
     */
    CarreraResponseDTO buscarPorNombre(String nombre);
    
    /**
     * Busca carreras por duración en semestres.
     * @param duracion Duración en semestres
     * @return Lista de carreras con esa duración
     */
    List<CarreraResponseDTO> buscarPorDuracion(Integer duracion);
}