package com.universidad.api.application.mappers;

import com.universidad.api.application.dto.FacultadRequestDTO;
import com.universidad.api.application.dto.FacultadResponseDTO;
import com.universidad.api.domain.entities.Facultad;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper para Facultad - Patrón MVC.
 * MAPPER: Convierte entre diferentes representaciones de datos
 * - Transforma entidades (Model) a DTOs (View)
 * - Convierte DTOs de entrada a entidades
 * - Mantiene separación entre capas del MVC
 * - Utiliza MapStruct para mapeo automático
 */
@Mapper(
    componentModel = "spring",
    uses = {CarreraMapper.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface FacultadMapper {
    
    /**
     * Convierte una entidad Facultad a DTO de respuesta.
     * Model -> View: Transforma datos internos para presentación
     */
    @Mapping(target = "carreras", source = "carreras")
    FacultadResponseDTO toResponseDTO(Facultad facultad);
    
    /**
     * Convierte una lista de entidades Facultad a lista de DTOs de respuesta.
     */
    List<FacultadResponseDTO> toResponseDTOList(List<Facultad> facultades);
    
    /**
     * Convierte un DTO de request a entidad Facultad.
     * View -> Model: Transforma datos de entrada a entidad de dominio
     */
    @Mapping(target = "facultadId", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "carreras", ignore = true)
    Facultad toEntity(FacultadRequestDTO requestDTO);
    
    /**
     * Actualiza una entidad existente con los datos del DTO request.
     * Mantiene la integridad del Model al actualizar solo campos permitidos
     */
    @Mapping(target = "facultadId", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "carreras", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(FacultadRequestDTO requestDTO, @MappingTarget Facultad facultad);
}