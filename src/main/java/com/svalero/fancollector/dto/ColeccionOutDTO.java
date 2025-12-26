package com.svalero.fancollector.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ColeccionOutDTO {

    private Long id;
    private Long idCreador;
    private String nombreCreador;
    private String nombre;
    private String descripcion;
    private String categoria;
    private String imagenPortada;
    private LocalDateTime fechaCreacion;
    private boolean esPublica;
    private boolean usableComoPlantilla;
}
