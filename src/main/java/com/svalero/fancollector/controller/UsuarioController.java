package com.svalero.fancollector.controller;

import com.svalero.fancollector.domain.enums.RolUsuario;
import com.svalero.fancollector.dto.UsuarioInDTO;
import com.svalero.fancollector.dto.UsuarioOutDTO;
import com.svalero.fancollector.dto.patches.UsuarioPasswordDTO;
import com.svalero.fancollector.exception.domain.UsuarioNoEncontradoException;
import com.svalero.fancollector.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioOutDTO> crearUsuario(@Valid @RequestBody UsuarioInDTO usuarioInDTO) {
        UsuarioOutDTO nuevoUsuario = usuarioService.crearUsuario(usuarioInDTO);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioOutDTO>> listarUsuarios(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) RolUsuario rol
    ) {
        List<UsuarioOutDTO> listaUsuarios = usuarioService.listarUsuarios(nombre, email, rol);
        return ResponseEntity.ok(listaUsuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioOutDTO> obtenerUsuario(
            @PathVariable long id)
            throws UsuarioNoEncontradoException {
        UsuarioOutDTO usuarioEncontrado  = usuarioService.buscarUsuarioPorId(id);
        return ResponseEntity.ok(usuarioEncontrado );
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioOutDTO> modificarUsuario(
            @PathVariable long id, @Valid @RequestBody UsuarioInDTO usuarioInDTO)
            throws UsuarioNoEncontradoException {
        UsuarioOutDTO usuarioModificado = usuarioService.modificarUsuario(id, usuarioInDTO);
        return ResponseEntity.ok(usuarioModificado);
    }

    @PatchMapping("/{id}/contrasena")
    public ResponseEntity<UsuarioOutDTO> actualizarContrasena(
            @PathVariable long id,
            @Valid @RequestBody UsuarioPasswordDTO passwordDTO)
            throws UsuarioNoEncontradoException {

        UsuarioOutDTO usuarioActualizado = usuarioService.actualizarContrasena(id, passwordDTO.getContrasena());
        return ResponseEntity.ok(usuarioActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarUsuario(
            @PathVariable long id)
            throws UsuarioNoEncontradoException {
        usuarioService.borrarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
