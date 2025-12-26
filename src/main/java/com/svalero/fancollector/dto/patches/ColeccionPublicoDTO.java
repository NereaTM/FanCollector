package com.svalero.fancollector.dto.patches;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ColeccionPublicoDTO {

    @NotNull(message = "El campo esPublica no puede ser null")
    private Boolean esPublica;
}