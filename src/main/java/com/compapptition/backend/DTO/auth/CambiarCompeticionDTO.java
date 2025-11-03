package com.compapptition.backend.DTO.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CambiarCompeticionDTO {
    @NotNull
    private Long competicionId;
}
