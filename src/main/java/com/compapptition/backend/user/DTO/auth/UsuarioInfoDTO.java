package com.compapptition.backend.user.DTO.auth;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UsuarioInfoDTO {
    private Long userId;
    private String username;
    private String email;
    private List<CompeticionContextoDTO> competiciones;
    private Long competicionActual;
}