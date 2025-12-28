package com.svalero.fancollector.controller;

import com.svalero.fancollector.dto.UsuarioColeccionInDTO;
import com.svalero.fancollector.dto.UsuarioColeccionOutDTO;
import com.svalero.fancollector.dto.UsuarioColeccionPutDTO;
import com.svalero.fancollector.dto.patches.UsuarioColeccionFavoritaDTO;
import com.svalero.fancollector.exception.domain.ColeccionNoEncontradaException;
import com.svalero.fancollector.exception.domain.UsuarioColeccionNoEncontradoException;
import com.svalero.fancollector.exception.domain.UsuarioNoEncontradoException;
import com.svalero.fancollector.exception.validation.RelacionYaExisteException;
import com.svalero.fancollector.service.UsuarioColeccionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/usuario-colecciones")
public class UsuarioColeccionController {

    @Autowired
    private UsuarioColeccionService usuarioColeccionService;

    @PostMapping
    public ResponseEntity<UsuarioColeccionOutDTO> crear(
            @Valid @RequestBody UsuarioColeccionInDTO dto
    ) throws UsuarioNoEncontradoException, ColeccionNoEncontradaException,RelacionYaExisteException {
        return new ResponseEntity<>(usuarioColeccionService.crear(dto),HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioColeccionOutDTO>> listar(
            @RequestParam(required = false) Long idUsuario,
            @RequestParam(required = false) Long idColeccion,
            @RequestParam(required = false) Boolean soloFavoritas
    ) {
        return ResponseEntity.ok(usuarioColeccionService.listar(idUsuario, idColeccion, soloFavoritas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioColeccionOutDTO> buscar(@PathVariable Long id)
            throws UsuarioColeccionNoEncontradoException {
        return ResponseEntity.ok(usuarioColeccionService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioColeccionOutDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioColeccionPutDTO dto
    ) throws UsuarioColeccionNoEncontradoException {
        return ResponseEntity.ok(usuarioColeccionService.actualizar(id, dto));
    }

    @PatchMapping("/{id}/favorita")
    public ResponseEntity<UsuarioColeccionOutDTO> actualizarFavorita(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioColeccionFavoritaDTO dto
    ) throws UsuarioColeccionNoEncontradoException {
        return ResponseEntity.ok(usuarioColeccionService.actualizarFavorita(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id)
            throws UsuarioColeccionNoEncontradoException {
        usuarioColeccionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
