package com.compapptition.backend.DTO.auth;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LoginResponseDTO {
    private String token;
    private Long userId;
    private String username;
    private String email;
    private List<CompeticionContextoDTO> competiciones;
    private Long competicionActual;
}
