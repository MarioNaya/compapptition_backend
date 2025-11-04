package com.compapptition.backend.user.controller;

import com.compapptition.backend.user.DTO.auth.*;
import com.compapptition.backend.user.security.SecurityUser;
import com.compapptition.backend.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * Login inicial con todas las competiciones
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    /**
     * Cambiar de competición (sin reautenticar)
     */
    @PostMapping("/cambiar-competicion")
    public ResponseEntity<CambioCompeticionResponseDTO> cambiarCompeticion(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody CambiarCompeticionDTO dto) {

        String token = authHeader.substring(7); // Quitar "Bearer "
        return ResponseEntity.ok(
                authService.cambiarCompeticion(token, dto)
        );
    }

    /**
     * Refrescar lista de competiciones
     */
    @PostMapping("/refrescar-competiciones")
    public ResponseEntity<LoginResponseDTO> refrescarCompeticiones(
            @AuthenticationPrincipal SecurityUser securityUser) {
        return ResponseEntity.ok(
                authService.refrescarCompeticiones(securityUser.getUser().getId())
        );
    }

    /**
     * Obtener contexto actual del usuario
     */
    @GetMapping("/contexto-actual")
    public ResponseEntity<CompeticionContextoDTO> getContextoActual(
            @AuthenticationPrincipal SecurityUser securityUser) {

        CompeticionContextoDTO contexto = securityUser.getContextoActual();
        return ResponseEntity.ok(contexto);
    }

    /**
     * Obtener información del usuario autenticado
     */
    @GetMapping("/me")
    public ResponseEntity<UsuarioInfoDTO> getUsuarioActual(
            @AuthenticationPrincipal SecurityUser securityUser) {

        return ResponseEntity.ok(
                UsuarioInfoDTO.builder()
                        .userId(securityUser.getUser().getId())
                        .username(securityUser.getUsername())
                        .email(securityUser.getUser().getEmail())
                        .competiciones(securityUser.getCompeticiones())
                        .competicionActual(securityUser.getCompeticionActual())
                        .build()
        );
    }
}