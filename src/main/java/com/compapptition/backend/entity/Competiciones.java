package com.compapptition.backend.entity;

import com.compapptition.backend.user.entity.RolCompeticion;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "competiciones")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = {"rolesCompeticion"})
@EqualsAndHashCode(exclude = {"rolesCompeticion"})
public class Competiciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String competitionName;

    @Column(length = 500)
    private String descripcion;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EstadoCompeticion estado = EstadoCompeticion.CREADA;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;



    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "competicion", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RolCompeticion> rolesCompeticion = new ArrayList<>();

    public enum EstadoCompeticion {
        CREADA,
        INSCRIPCIONES_ABIERTAS,
        EN_CURSO,
        FINALIZADA,
        CANCELADA
    }
}
