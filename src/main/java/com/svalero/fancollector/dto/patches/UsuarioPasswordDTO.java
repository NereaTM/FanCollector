package com.svalero.fancollector.dto.patches;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioPasswordDTO {

    @NotBlank (message = "La contraseña no puede estar en blanco")
    @Size(min = 4, message = "La contraseña debe tener al menos 4 digitos")
    private String contrasena;

}