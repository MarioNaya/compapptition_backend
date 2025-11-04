package com.compapptition.backend.user.DTO.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CambioCompeticionResponseDTO {
    private String token;
    private CompeticionContextoDTO competicionActual;
}
