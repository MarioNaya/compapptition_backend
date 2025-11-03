package com.compapptition.backend.mapper;

import com.compapptition.backend.DTO.auth.CompeticionContextoDTO;
import com.compapptition.backend.entity.RolCompeticion;
import com.compapptition.backend.entity.Roles;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompeticionMapper {

    public CompeticionContextoDTO toContextoDTO(RolCompeticion rolCompeticion) {
        return CompeticionContextoDTO.builder()
                .competicionId(rolCompeticion.getCompeticion().getId())
                .nombre(rolCompeticion.getCompeticion().getCompeticionNombre())
                .role(rolCompeticion.getRole().getRoleNombre())  // ✅ Obtener el enum
                .permisos(getPermisosPorRole(rolCompeticion.getRole().getRoleNombre()))
                .build();
    }

    public List<CompeticionContextoDTO> toContextoDTOList(List<RolCompeticion> roles) {
        return roles.stream()
                .map(this::toContextoDTO)
                .collect(Collectors.toList());
    }

    private List<String> getPermisosPorRole(Roles.EnumRoles role) {
        return switch (role) {
            case ADMIN -> List.of(
                    "crear", "editar", "eliminar", "ver",
                    "gestionar_usuarios", "configurar_competicion"
            );
            case MANAGER -> List.of(
                    "crear", "editar", "ver",
                    "gestionar_participantes", "gestionar_jueces"
            );
            case MESA -> List.of(
                    "evaluar", "ver", "comentar", "puntuar", "validar"
            );
            case PARTICIPANTE -> List.of(
                    "participar", "ver", "comentar", "subir_archivos"
            );
            case ESPECTADOR -> List.of("ver");
        };
    }
}