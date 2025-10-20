package com.universidad.api.domain.repositories;

import com.universidad.api.domain.entities.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Carrera.
 * Sigue el principio ISP (Interface Segregation Principle) - métodos específicos y cohesivos.
 */
@Repository
public interface CarreraRepository extends JpaRepository<Carrera, Long> {
    
    /**
     * Busca carreras activas.
     * @return Lista de carreras activas
     */
    List<Carrera> findByActivoTrue();
    
    /**
     * Busca carreras por facultad.
     * @param facultadId ID de la facultad
     * @return Lista de carreras de la facultad
     */
    List<Carrera> findByFacultad_FacultadId(Long facultadId);
    
    /**
     * Busca carreras activas por facultad.
     * @param facultadId ID de la facultad
     * @return Lista de carreras activas de la facultad
     */
    List<Carrera> findByFacultad_FacultadIdAndActivoTrue(Long facultadId);
    
    /**
     * Busca una carrera por nombre (case-insensitive).
     * @param nombre Nombre de la carrera
     * @return Optional con la carrera encontrada
     */
    @Query("SELECT c FROM Carrera c WHERE UPPER(c.nombre) = UPPER(:nombre)")
    Optional<Carrera> findByNombreIgnoreCase(@Param("nombre") String nombre);
    
    /**
     * Busca carreras por duración en semestres.
     * @param duracionSemestres Duración en semestres
     * @return Lista de carreras con esa duración
     */
    List<Carrera> findByDuracionSemestres(Integer duracionSemestres);
    
    /**
     * Busca carreras por título otorgado (case-insensitive).
     * @param tituloOtorgado Título otorgado
     * @return Lista de carreras que otorgan ese título
     */
    List<Carrera> findByTituloOtorgadoContainingIgnoreCase(String tituloOtorgado);
    
    /**
     * Verifica si existe una carrera con el nombre dado.
     * @param nombre Nombre a verificar
     * @return true si existe, false caso contrario
     */
    boolean existsByNombreIgnoreCase(String nombre);
    
    /**
     * Verifica si existe una carrera con el nombre dado, excluyendo un ID específico.
     * Útil para validaciones en actualizaciones.
     * @param nombre Nombre a verificar
     * @param carreraId ID a excluir de la búsqueda
     * @return true si existe otra carrera con ese nombre
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Carrera c " +
           "WHERE UPPER(c.nombre) = UPPER(:nombre) AND c.carreraId != :carreraId")
    boolean existsByNombreIgnoreCaseAndCarreraIdNot(@Param("nombre") String nombre, 
                                                    @Param("carreraId") Long carreraId);
    
    /**
     * Cuenta las carreras de una facultad específica.
     * @param facultadId ID de la facultad
     * @return Número de carreras en la facultad
     */
    @Query("SELECT COUNT(c) FROM Carrera c WHERE c.facultad.facultadId = :facultadId")
    long countByFacultadId(@Param("facultadId") Long facultadId);
}