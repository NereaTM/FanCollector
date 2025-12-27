package com.svalero.fancollector.controller;

import com.svalero.fancollector.dto.ItemInDTO;
import com.svalero.fancollector.dto.ItemOutDTO;
import com.svalero.fancollector.dto.ItemPutDTO;
import com.svalero.fancollector.dto.patches.ItemRarezaDTO;
import com.svalero.fancollector.exception.domain.ColeccionNoEncontradaException;
import com.svalero.fancollector.exception.domain.ItemNoEncontradoException;
import com.svalero.fancollector.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemOutDTO> crearItem(@Valid @RequestBody ItemInDTO itemInDTO)
            throws ColeccionNoEncontradaException {
        ItemOutDTO nuevo = itemService.crearItem(itemInDTO);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ItemOutDTO>> listarItems(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String rareza,
            @RequestParam(required = false) Long idColeccion) {
        List<ItemOutDTO> items = itemService.listarItems(nombre, tipo, rareza, idColeccion);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemOutDTO> buscarItem(@PathVariable Long id)
            throws ItemNoEncontradoException {
        ItemOutDTO item = itemService.buscarItemPorId(id);
        return ResponseEntity.ok(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemOutDTO> actualizarItem(
            @PathVariable Long id,
            @Valid @RequestBody ItemPutDTO itemPutDTO )
            throws ItemNoEncontradoException, ColeccionNoEncontradaException {
        ItemOutDTO actualizado = itemService.actualizarItem(id, itemPutDTO);
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/{id}/rareza")
    public ResponseEntity<ItemOutDTO> actualizarRareza(
            @PathVariable Long id,
            @Valid @RequestBody ItemRarezaDTO rarezaDTO)
            throws ItemNoEncontradoException {
        ItemOutDTO itemActualizado = itemService.actualizarRareza(
                id,
                rarezaDTO.getRareza());
        return ResponseEntity.ok(itemActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarItem(@PathVariable Long id)
            throws ItemNoEncontradoException {
        itemService.eliminarItem(id);
        return ResponseEntity.noContent().build();
    }
}