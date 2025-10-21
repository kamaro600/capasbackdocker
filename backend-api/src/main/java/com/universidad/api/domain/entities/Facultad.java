package com.universidad.api.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad Facultad - Patrón MVC.
 * MODEL: Representa los datos y la lógica de negocio en el patrón MVC
 * - Define la estructura de datos de Facultad
 * - Maneja la persistencia con JPA/Hibernate
 * - Contiene las reglas de negocio a nivel de entidad
 * - Representa el estado y comportamiento del dominio
 */
@Entity
@Table(name = "facultad")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Facultad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facultad_id")
    private Long facultadId;
    
    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "ubicacion", length = 100)
    private String ubicacion;
    
    @Column(name = "decano", length = 100)
    private String decano;
    
    @CreatedDate
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;
    
    @Builder.Default
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
    
    // Relación One-to-Many con Carrera
    @OneToMany(mappedBy = "facultad", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Carrera> carreras;
    
    @PrePersist
    protected void onCreate() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
        if (activo == null) {
            activo = true;
        }
    }
    
    // Método de conveniencia para agregar carrera (mantiene consistencia bidireccional)
    public void addCarrera(Carrera carrera) {
        if (carreras != null) {
            carreras.add(carrera);
            carrera.setFacultad(this);
        }
    }
    
    // Método de conveniencia para remover carrera
    public void removeCarrera(Carrera carrera) {
        if (carreras != null) {
            carreras.remove(carrera);
            carrera.setFacultad(null);
        }
    }
}