package com.compapptition.backend.user.security;

import com.compapptition.backend.user.DTO.auth.CompeticionContextoDTO;
import com.compapptition.backend.user.entity.Roles;
import com.compapptition.backend.user.entity.Usuarios;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@Data
public class SecurityUser implements UserDetails {
    private Usuarios user;

    // NUEVO: Lista de todos los contextos disponibles
    private List<CompeticionContextoDTO> competiciones;

    // NUEVO: Competición actual seleccionada
    private Long competicionActual;

    // Constructor sin competiciones (para login básico)
    public SecurityUser(Usuarios user) {
        this.user = user;
        this.competiciones = Collections.emptyList();
        this.competicionActual = null;
    }

    // Constructor con competiciones pero sin selección
    public SecurityUser(Usuarios user, List<CompeticionContextoDTO> competiciones) {
        this.user = user;
        this.competiciones = competiciones;
        this.competicionActual = null;
    }

    // Constructor completo con competición actual
    public SecurityUser(Usuarios user, List<CompeticionContextoDTO> competiciones, Long competicionActual) {
        this.user = user;
        this.competiciones = competiciones;
        this.competicionActual = competicionActual;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (competicionActual == null) {
            // Sin competición seleccionada: sin roles
            return Collections.emptyList();
        }

        // Buscar el contexto de la competición actual
        return competiciones.stream()
                .filter(c -> c.getCompeticionId().equals(competicionActual))
                .findFirst()
                .map(c -> Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + c.getRole().name())
                ))
                .orElse(Collections.emptyList());
    }

    // Metodo para obtener el role actual
    public Roles.EnumRoles getRoleActual() {
        if (competicionActual == null) return null;

        return competiciones.stream()
                .filter(c -> c.getCompeticionId().equals(competicionActual))
                .findFirst()
                .map(CompeticionContextoDTO::getRole)
                .orElse(null);
    }

    // Metodo para obtener contexto actual
    public CompeticionContextoDTO getContextoActual() {
        if (competicionActual == null) return null;

        return competiciones.stream()
                .filter(c -> c.getCompeticionId().equals(competicionActual))
                .findFirst()
                .orElse(null);
    }

    // Verificar si tiene acceso a una competición específica
    public boolean tieneAccesoACompeticion(Long competicionId) {
        return competiciones.stream()
                .anyMatch(c -> c.getCompeticionId().equals(competicionId));
    }

    // Obtener role para una competición específica
    public Roles.EnumRoles getRoleEnCompeticion(Long competicionId) {
        return competiciones.stream()
                .filter(c -> c.getCompeticionId().equals(competicionId))
                .findFirst()
                .map(CompeticionContextoDTO::getRole)
                .orElse(null);
    }

    // Metodo útil para verificar permisos por nivel
    public boolean tieneNivelMinimo(Roles.EnumRoles roleRequerido, Long competicionId) {
        Roles.EnumRoles roleUsuario = getRoleEnCompeticion(competicionId);
        if (roleUsuario == null) return false;

        return getNivel(roleUsuario) >= getNivel(roleRequerido);
    }

    private int getNivel(Roles.EnumRoles role) {
        return switch (role) {
            case ADMIN -> 5;
            case MANAGER -> 4;
            case MESA -> 3;
            case PARTICIPANTE -> 2;
            case ESPECTADOR -> 1;
        };
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isEnabled() {
        return user.getActivo() != null && user.getActivo();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
