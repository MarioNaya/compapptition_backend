package com.compapptition.backend.DTO.auth;

import com.compapptition.backend.entity.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompeticionContextoDTO {
    private Long competicionId;
    private String nombre;
    private Roles.EnumRoles role;
    private List<String> permisos;
}
