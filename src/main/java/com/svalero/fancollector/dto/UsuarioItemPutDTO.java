package com.svalero.fancollector.dto;

import com.svalero.fancollector.domain.enums.EstadoItem;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UsuarioItemPutDTO {

    private EstadoItem estado;
    private Boolean esVisible;
    private String notas;

    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer cantidad;
}