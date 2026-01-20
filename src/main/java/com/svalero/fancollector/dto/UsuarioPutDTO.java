package com.svalero.fancollector.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioPutDTO {
    @NotBlank(message = "El nombre no puede estar en blanco")
    private String nombre;

    @Email(message = "Ejemplo email <ejemplo@gmail.com>")
    @NotBlank(message = "El email no puede estar en blanco")
    private String email;

    private String urlAvatar;
    private String descripcion;
    private String contactoPublico;
}
