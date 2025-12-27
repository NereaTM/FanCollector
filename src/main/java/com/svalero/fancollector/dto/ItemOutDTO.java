package com.svalero.fancollector.dto;

import com.svalero.fancollector.domain.enums.RarezaItem;
import lombok.Data;

@Data
public class ItemOutDTO {

    private Long id;
    private Long idColeccion;
    private String nombreColeccion;
    private String nombre;
    private String descripcion;
    private String imagenUrl;
    private String tipo;
    private RarezaItem rareza;
    private Integer anioLanzamiento;
}
