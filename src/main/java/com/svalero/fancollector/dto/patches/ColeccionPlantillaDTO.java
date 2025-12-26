package com.svalero.fancollector.dto.patches;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ColeccionPlantillaDTO {

    @NotNull(message = "El campo usableComoPlantilla no puede ser null")
    private Boolean usableComoPlantilla;
}