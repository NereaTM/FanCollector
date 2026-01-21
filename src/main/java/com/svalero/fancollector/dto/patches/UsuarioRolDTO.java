package com.svalero.fancollector.dto.patches;

import com.svalero.fancollector.domain.enums.RolUsuario;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioRolDTO {

    @NotNull(message = "El rol no puede ser nulo")
    private RolUsuario rol;
}
