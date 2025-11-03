package com.compapptition.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "rol_competicion", uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "competicion_id"}))
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = {"usuario", "competicion"})
@EqualsAndHashCode(exclude = {"usuario", "competicion"})
public class RolCompeticion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuarios usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Roles role;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "competicion_id", nullable = false)
    private Competiciones competicion;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @Column(name = "fecha_asignacion", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime fechaAsignacion = LocalDateTime.now();

    @Column(name = "fecha_desactivacion")
    private LocalDateTime fechaDesactivacion;

    @Column(name = "asignado_por")
    private Long asignadoPorUsuarioId;
}
