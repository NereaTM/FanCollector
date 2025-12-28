package com.svalero.fancollector.dto;

import com.svalero.fancollector.domain.enums.EstadoItem;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UsuarioItemOutDTO {

    private Long id;
    private Long idUsuario;
    private String nombreUsuario;
    private Long idColeccion;
    private String nombreColeccion;
    private Long idItem;
    private String nombreItem;
    private EstadoItem estado;
    private Integer cantidad;
    private String notas;
    private boolean esVisible;
    private LocalDateTime fechaRegistro;
}