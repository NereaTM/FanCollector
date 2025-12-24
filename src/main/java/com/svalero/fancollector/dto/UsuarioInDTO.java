package com.svalero.fancollector.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioInDTO {

    @NotBlank(message = "El nombre no puede estar en blanco")
    private String nombre;

    @Email(message = "Formato valido del email <ejemplo@gmail.com>")
    @NotBlank(message = "El email no puede estar en blanco")
    private String email;

    @NotBlank(message = "La contraseña no puede estar en blanco")
    @Size(min = 4, message = "La contraseña debe tener al menos 4 caracteres")
    private String contrasena;

    private String urlAvatar;
    private String descripcion;
    private String contactoPublico;
}
