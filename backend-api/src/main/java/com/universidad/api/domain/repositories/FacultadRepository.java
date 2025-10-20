package com.universidad.api.domain.repositories;

import com.universidad.api.domain.entities.Facultad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Facultad.
 * Sigue el principio ISP (Interface Segregation Principle) - métodos específicos y cohesivos.
 */
@Repository
public interface FacultadRepository extends JpaRepository<Facultad, Long> {
    
    /**
     * Busca facultades activas.
     * @return Lista de facultades activas
     */
    List<Facultad> findByActivoTrue();
    
    /**
     * Busca una facultad por nombre (case-insensitive).
     * @param nombre Nombre de la facultad
     * @return Optional con la facultad encontrada
     */
    @Query("SELECT f FROM Facultad f WHERE UPPER(f.nombre) = UPPER(:nombre)")
    Optional<Facultad> findByNombreIgnoreCase(@Param("nombre") String nombre);
    
    /**
     * Busca facultades por decano.
     * @param decano Nombre del decano
     * @return Lista de facultades del decano
     */
    List<Facultad> findByDecanoContainingIgnoreCase(String decano);
    
    /**
     * Busca facultades por ubicación.
     * @param ubicacion Ubicación de la facultad
     * @return Lista de facultades en esa ubicación
     */
    List<Facultad> findByUbicacionContainingIgnoreCase(String ubicacion);
    
    /**
     * Verifica si existe una facultad con el nombre dado.
     * @param nombre Nombre a verificar
     * @return true si existe, false caso contrario
     */
    boolean existsByNombreIgnoreCase(String nombre);
    
    /**
     * Verifica si existe una facultad con el nombre dado, excluyendo un ID específico.
     * Útil para validaciones en actualizaciones.
     * @param nombre Nombre a verificar
     * @param facultadId ID a excluir de la búsqueda
     * @return true si existe otra facultad con ese nombre
     */
    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Facultad f " +
           "WHERE UPPER(f.nombre) = UPPER(:nombre) AND f.facultadId != :facultadId")
    boolean existsByNombreIgnoreCaseAndFacultadIdNot(@Param("nombre") String nombre, 
                                                     @Param("facultadId") Long facultadId);
}