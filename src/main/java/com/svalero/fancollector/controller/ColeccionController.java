package com.svalero.fancollector.controller;

import com.svalero.fancollector.dto.ColeccionInDTO;
import com.svalero.fancollector.dto.ColeccionOutDTO;
import com.svalero.fancollector.dto.ColeccionPutDTO;
import com.svalero.fancollector.dto.patches.ColeccionPlantillaDTO;
import com.svalero.fancollector.dto.patches.ColeccionPublicoDTO;
import com.svalero.fancollector.exception.domain.ColeccionNoEncontradaException;
import com.svalero.fancollector.exception.domain.UsuarioNoEncontradoException;
import com.svalero.fancollector.service.ColeccionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/colecciones")
public class ColeccionController {

    @Autowired
    private ColeccionService coleccionService;

    @PostMapping
    public ResponseEntity<ColeccionOutDTO> crearColeccion(@Valid @RequestBody ColeccionInDTO dto)
            throws UsuarioNoEncontradoException {
        return new ResponseEntity<>(coleccionService.crearColeccion(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ColeccionOutDTO>> listarColecciones(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Long idCreador,
            @RequestParam(required = false) String nombreCreador) {
        return ResponseEntity.ok
                (coleccionService.listarColecciones(nombre, categoria, idCreador, nombreCreador));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColeccionOutDTO> buscarColeccion(@PathVariable Long id)
            throws ColeccionNoEncontradaException {
        return ResponseEntity.ok(coleccionService.buscarColeccionPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ColeccionOutDTO> actualizarColeccion(
            @PathVariable Long id,
            @Valid @RequestBody ColeccionPutDTO dto
    ) throws ColeccionNoEncontradaException, UsuarioNoEncontradoException {
        return ResponseEntity.ok(coleccionService.actualizarColeccion(id, dto));
    }

    @PatchMapping("/{id}/publico")
    public ResponseEntity<ColeccionOutDTO> actualizarEsPublica(
            @PathVariable Long id,
            @Valid @RequestBody ColeccionPublicoDTO publicoDTO)
            throws ColeccionNoEncontradaException {

        ColeccionOutDTO coleccionActualizada = coleccionService.actualizarEsPublica(
                id,
                publicoDTO.getEsPublica());
        return ResponseEntity.ok(coleccionActualizada);
    }

    @PatchMapping("/{id}/plantilla")
    public ResponseEntity<ColeccionOutDTO> actualizarUsableComoPlantilla(
            @PathVariable Long id,
            @Valid @RequestBody ColeccionPlantillaDTO plantillaDTO)
            throws ColeccionNoEncontradaException {

        ColeccionOutDTO coleccionActualizada = coleccionService.actualizarUsableComoPlantilla(
                id,
                plantillaDTO.getUsableComoPlantilla()
        );
        return ResponseEntity.ok(coleccionActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarColeccion(@PathVariable Long id)
            throws ColeccionNoEncontradaException {
        coleccionService.eliminarColeccion(id);
        return ResponseEntity.noContent().build();
    }
}
