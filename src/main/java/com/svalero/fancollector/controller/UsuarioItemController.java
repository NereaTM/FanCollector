package com.svalero.fancollector.controller;

import com.svalero.fancollector.domain.enums.EstadoItem;
import com.svalero.fancollector.dto.UsuarioItemInDTO;
import com.svalero.fancollector.dto.UsuarioItemOutDTO;
import com.svalero.fancollector.dto.UsuarioItemPutDTO;
import com.svalero.fancollector.dto.patches.UsuarioItemVisibleDTO;
import com.svalero.fancollector.exception.domain.ColeccionNoEncontradaException;
import com.svalero.fancollector.exception.domain.ItemNoEncontradoException;
import com.svalero.fancollector.exception.domain.UsuarioItemNoEncontradoException;
import com.svalero.fancollector.exception.domain.UsuarioNoEncontradoException;
import com.svalero.fancollector.service.UsuarioItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/usuario-items")
public class UsuarioItemController {

    @Autowired
    private UsuarioItemService usuarioItemService;

    @PostMapping
    public ResponseEntity<UsuarioItemOutDTO> crear(
            @Valid @RequestBody UsuarioItemInDTO dto
    ) throws UsuarioNoEncontradoException, ItemNoEncontradoException, ColeccionNoEncontradaException {
        return new ResponseEntity<>(usuarioItemService.crear(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioItemOutDTO>> listar(
            @RequestParam(required = false) Long idUsuario,
            @RequestParam(required = false) Long idItem,
            @RequestParam(required = false) Long idColeccion,
            @RequestParam(required = false) EstadoItem estado,
            @RequestParam(required = false) Boolean esVisible
    ) {
        return ResponseEntity.ok(usuarioItemService.listar(idUsuario, idItem, idColeccion, estado, esVisible));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioItemOutDTO> buscar(@PathVariable Long id)
            throws UsuarioItemNoEncontradoException {
        return ResponseEntity.ok( usuarioItemService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioItemOutDTO> actualizarCompleto(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioItemPutDTO dto)
            throws UsuarioItemNoEncontradoException {
        return ResponseEntity.ok(usuarioItemService.actualizarCompleto(id, dto));
    }

    @PatchMapping("/{id}/visible")
    public ResponseEntity<UsuarioItemOutDTO> actualizarVisibilidad(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioItemVisibleDTO visibleDTO)
            throws UsuarioItemNoEncontradoException {
        return ResponseEntity.ok(usuarioItemService.actualizarVisibilidad(id, visibleDTO.getEsVisible()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id)
            throws UsuarioItemNoEncontradoException {
        usuarioItemService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
