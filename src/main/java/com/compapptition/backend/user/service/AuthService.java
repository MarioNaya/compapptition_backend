package com.compapptition.backend.user.service;

import com.compapptition.backend.user.DTO.auth.*;
import com.compapptition.backend.user.entity.Usuarios;
import com.compapptition.backend.user.entity.RolCompeticion;
import com.compapptition.backend.user.mapper.CompeticionMapper;
import com.compapptition.backend.user.repository.UsuariosRepository;
import com.compapptition.backend.user.repository.RolCompeticionRepository;
import com.compapptition.backend.user.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuariosRepository usuarioRepository;
    private final RolCompeticionRepository rolCompeticionRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CompeticionMapper competicionMapper;

    /**
     * Login con todos los contextos
     */
    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO dto) {
        // Autenticar
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsername(),
                        dto.getPassword()
                )
        );

        Usuarios usuario = usuarioRepository.findByUserName(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtener participaciones con JOIN FETCH para evitar N+1
        List<RolCompeticion> participaciones =
                rolCompeticionRepository.findByUsuarioIdWithCompeticion(usuario.getId());

        // Convertir a DTOs usando el mapper
        List<CompeticionContextoDTO> competiciones =
                competicionMapper.toContextoDTOList(participaciones);

        // Generar token con todos los contextos
        String token = jwtService.generateToken(usuario, competiciones, null);

        return LoginResponseDTO.builder()
                .token(token)
                .userId(usuario.getId())
                .username(usuario.getUserName())
                .email(usuario.getEmail())
                .competiciones(competiciones)
                .competicionActual(null)
                .build();
    }

    /**
     * Cambiar a otra competición (sin reautenticar)
     */
    @Transactional(readOnly = true)
    public CambioCompeticionResponseDTO cambiarCompeticion(
            String currentToken,
            CambiarCompeticionDTO dto) {

        List<CompeticionContextoDTO> competiciones =
                jwtService.getCompeticionesFromToken(currentToken);

        CompeticionContextoDTO competicionSeleccionada = competiciones.stream()
                .filter(c -> c.getCompeticionId().equals(dto.getCompeticionId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No tienes acceso a esta competición"));

        String nuevoToken = jwtService.updateCompeticionActual(
                currentToken,
                dto.getCompeticionId()
        );

        return CambioCompeticionResponseDTO.builder()
                .token(nuevoToken)
                .competicionActual(competicionSeleccionada)
                .build();
    }

    @Transactional(readOnly = true)
    public LoginResponseDTO refrescarCompeticiones(Long userId) {
        Usuarios usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtener competiciones actualizadas
        List<RolCompeticion> participaciones =
                rolCompeticionRepository.findByUsuarioIdWithCompeticion(userId);

        List<CompeticionContextoDTO> competiciones =
                competicionMapper.toContextoDTOList(participaciones);

        // Generar nuevo token con competiciones actualizadas
        String token = jwtService.generateToken(usuario, competiciones, null);

        return LoginResponseDTO.builder()
                .token(token)
                .userId(usuario.getId())
                .username(usuario.getUserName())
                .email(usuario.getEmail())
                .competiciones(competiciones)
                .competicionActual(null)
                .build();
    }
}