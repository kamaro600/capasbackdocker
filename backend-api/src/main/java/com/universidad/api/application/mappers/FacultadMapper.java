package com.universidad.api.application.mappers;

import com.universidad.api.application.dto.FacultadRequestDTO;
import com.universidad.api.application.dto.FacultadResponseDTO;
import com.universidad.api.domain.entities.Facultad;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper para Facultad siguiendo el principio ISP (Interface Segregation Principle).
 * Separa las responsabilidades de mapeo en métodos específicos.
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
     * Sigue el principio SRP - una sola responsabilidad de conversión.
     */
    @Mapping(target = "carreras", source = "carreras")
    FacultadResponseDTO toResponseDTO(Facultad facultad);
    
    /**
     * Convierte una lista de entidades Facultad a lista de DTOs de respuesta.
     */
    List<FacultadResponseDTO> toResponseDTOList(List<Facultad> facultades);
    
    /**
     * Convierte un DTO de request a entidad Facultad.
     * Ignora campos que no deben ser mapeados directamente.
     */
    @Mapping(target = "facultadId", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "carreras", ignore = true)
    Facultad toEntity(FacultadRequestDTO requestDTO);
    
    /**
     * Actualiza una entidad existente con los datos del DTO request.
     * Sigue el principio OCP - abierto para extensión, cerrado para modificación.
     */
    @Mapping(target = "facultadId", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "carreras", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(FacultadRequestDTO requestDTO, @MappingTarget Facultad facultad);
}