package com.svalero.fancollector.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "colecciones")
public class Coleccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "creado_por", nullable = false)
    private Usuario creador;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String nombre;

    @Lob
    @Column
    private String descripcion;

    @NotBlank
    @Column(length = 50)
    private String categoria;

    @Column(name = "imagen_portada", length = 500)
    private String imagenPortada;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "es_publica") //PATCH
    private boolean esPublica = false;

    @Column(name = "usable_como_plantilla") //PATCH
    private boolean usableComoPlantilla = false;
}
