package com.compapptition.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = {"rolesCompeticion"})
@EqualsAndHashCode(exclude = {"rolesCompeticion"})
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 50)
    private Roles.EnumRoles roleNombre;

    @Column(nullable = false, length = 100)
    private String displayNombre;

    @Column(nullable = false)
    @Builder.Default
    private Integer nivel = 1;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RolCompeticion> rolesCompeticion = new ArrayList<>();

    public enum EnumRoles {
        ADMIN,
        MANAGER,
        MESA,
        PARTICIPANTE,
        ESPECTADOR
    }
}
