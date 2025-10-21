package com.universidad.api.application.services;

import com.universidad.api.application.dto.FacultadRequestDTO;
import com.universidad.api.application.dto.FacultadResponseDTO;

import java.util.List;

/**
 * Interfaz del servicio de Facultad - Patrón MVC.
 * SERVICE: Capa de lógica de negocio en el patrón MVC
 * - Define las operaciones de negocio disponibles
 * - Abstrae la lógica de negocio del controlador
 * - Coordina entre el Controller y el Repository
 */
public interface FacultadService {
    
    /**
     * Crea una nueva facultad.
     * @param requestDTO Datos de la facultad a crear
     * @return DTO con los datos de la facultad creada
     */
    FacultadResponseDTO crear(FacultadRequestDTO requestDTO);
    
    /**
     * Obtiene una facultad por ID.
     * @param id ID de la facultad
     * @return DTO con los datos de la facultad
     */
    FacultadResponseDTO obtenerPorId(Long id);
    
    /**
     * Obtiene todas las facultades.
     * @return Lista de DTOs con todas las facultades
     */
    List<FacultadResponseDTO> obtenerTodas();
    
    /**
     * Obtiene solo las facultades activas.
     * @return Lista de DTOs con las facultades activas
     */
    List<FacultadResponseDTO> obtenerActivas();
    
    /**
     * Actualiza una facultad existente.
     * @param id ID de la facultad a actualizar
     * @param requestDTO Nuevos datos de la facultad
     * @return DTO con los datos de la facultad actualizada
     */
    FacultadResponseDTO actualizar(Long id, FacultadRequestDTO requestDTO);
    
    /**
     * Elimina lógicamente una facultad (marca como inactiva).
     * @param id ID de la facultad a eliminar
     */
    void eliminar(Long id);
    
    /**
     * Busca facultades por nombre.
     * @param nombre Nombre a buscar
     * @return DTO con la facultad encontrada si existe
     */
    FacultadResponseDTO buscarPorNombre(String nombre);
    
    /**
     * Busca facultades por decano.
     * @param decano Nombre del decano
     * @return Lista de facultades del decano
     */
    List<FacultadResponseDTO> buscarPorDecano(String decano);
}