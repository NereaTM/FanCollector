package com.svalero.fancollector.dto.patches;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioColeccionFavoritaDTO {

    @NotNull(message = "Favorita no puede ser nulo")
    private Boolean esFavorita;
}
