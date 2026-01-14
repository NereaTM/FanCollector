package com.svalero.fancollector.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioColeccionInDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;

    @NotNull(message = "El ID de la colección es obligatorio")
    private Long idColeccion;

    private Boolean esFavorita = false;
    private Boolean esCreador = false;
    private Boolean esVisible = true;

}