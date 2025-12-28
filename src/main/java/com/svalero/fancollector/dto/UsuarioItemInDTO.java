package com.svalero.fancollector.dto;

import com.svalero.fancollector.domain.enums.EstadoItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioItemInDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;

    @NotNull(message = "El ID del ítem es obligatorio")
    private Long idItem;

    @NotNull(message = "El ID de la colección es obligatorio")
    private Long idColeccion;

    private EstadoItem estado;
    private Boolean esVisible;
    private String notas;

    @Min(value = 0)
    private Integer cantidad;
}