package com.universidad.api.application.mappers;

import com.universidad.api.application.dto.CarreraRequestDTO;
import com.universidad.api.application.dto.CarreraResponseDTO;
import com.universidad.api.domain.entities.Carrera;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper para Carrera siguiendo el principio ISP (Interface Segregation Principle).
 * Separa las responsabilidades de mapeo en métodos específicos.
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CarreraMapper {
    
    /**
     * Convierte una entidad Carrera a DTO de respuesta.
     * Mapea campos específicos de la facultad asociada.
     */
    @Mapping(target = "facultadId", source = "facultad.facultadId")
    @Mapping(target = "nombreFacultad", source = "facultad.nombre")
    CarreraResponseDTO toResponseDTO(Carrera carrera);
    
    /**
     * Convierte una lista de entidades Carrera a lista de DTOs de respuesta.
     */
    List<CarreraResponseDTO> toResponseDTOList(List<Carrera> carreras);
    
    /**
     * Convierte un DTO de request a entidad Carrera.
     * Ignora campos que no deben ser mapeados directamente.
     */
    @Mapping(target = "carreraId", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "facultad", ignore = true)
    Carrera toEntity(CarreraRequestDTO requestDTO);
    
    /**
     * Actualiza una entidad existente con los datos del DTO request.
     * Mantiene la referencia a la facultad existente.
     */
    @Mapping(target = "carreraId", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "facultad", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(CarreraRequestDTO requestDTO, @MappingTarget Carrera carrera);
}