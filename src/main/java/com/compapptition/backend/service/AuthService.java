package com.compapptition.backend.service;

import com.compapptition.backend.DTO.auth.*;
import com.compapptition.backend.entity.Usuarios;
import com.compapptition.backend.entity.RolCompeticion;
import com.compapptition.backend.mapper.CompeticionMapper;
import com.compapptition.backend.repository.UsuariosRepository;
import com.compapptition.backend.repository.RolCompeticionRepository;
import com.compapptition.backend.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuariosRepository usuarioRepository;
    private final RolCompeticionRepository rolCompeticionRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CompeticionMapper competicionMapper;  // ✅ Inyectar el mapper

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
                .username(usuario.getUserNombre())
                .email(usuario.getEmail())
                .competiciones(competiciones)
                .competicionActual(null)
                .build();
    }

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
}