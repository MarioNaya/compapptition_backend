package com.compapptition.backend.user.DTO.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CambiarCompeticionDTO {
    @NotNull
    private Long competicionId;
}
