package com.universidad.api.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

/**
 * Entidad Carrera que representa una carrera universitaria.
 * Sigue el principio SRP (Single Responsibility Principle) - solo maneja datos de carrera.
 */
@Entity
@Table(name = "carrera")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Carrera {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carrera_id")
    private Long carreraId;
    
    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "duracion_semestres", nullable = false)
    private Integer duracionSemestres;
    
    @Column(name = "titulo_otorgado", length = 100)
    private String tituloOtorgado;
    
    @CreatedDate
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;
    
    @Builder.Default
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
    
    // Relación Many-to-One con Facultad
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facultad_id", nullable = false, 
                foreignKey = @ForeignKey(name = "fk_facultad"))
    private Facultad facultad;
    
    @PrePersist
    protected void onCreate() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
        if (activo == null) {
            activo = true;
        }
    }
    
    // Método de conveniencia para obtener el ID de la facultad
    public Long getFacultadId() {
        return facultad != null ? facultad.getFacultadId() : null;
    }
    
    // Método de conveniencia para obtener el nombre de la facultad
    public String getNombreFacultad() {
        return facultad != null ? facultad.getNombre() : null;
    }
}