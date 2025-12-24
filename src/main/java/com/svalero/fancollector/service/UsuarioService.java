package com.svalero.fancollector.service;

import com.svalero.fancollector.domain.enums.RolUsuario;
import com.svalero.fancollector.dto.UsuarioInDTO;
import com.svalero.fancollector.dto.UsuarioOutDTO;
import com.svalero.fancollector.exception.domain.UsuarioNoEncontradoException;

import java.util.List;

public interface UsuarioService {

    UsuarioOutDTO crearUsuario(UsuarioInDTO usuarioInDTO);

    UsuarioOutDTO buscarUsuarioPorId(long id) throws UsuarioNoEncontradoException;

    List<UsuarioOutDTO> listarUsuarios(String nombre, String email, RolUsuario rol);

    UsuarioOutDTO modificarUsuario(long id, UsuarioInDTO usuarioInDTO) throws UsuarioNoEncontradoException;

    void borrarUsuario(long id) throws UsuarioNoEncontradoException;

    UsuarioOutDTO actualizarContrasena(long id, String nuevaContrasena) throws UsuarioNoEncontradoException;
}