package com.svalero.fancollector.dto.patches;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioItemVisibleDTO {

    @NotNull(message = "El campo esVisible no puede ser nulo")
    private Boolean esVisible;
}