package com.svalero.fancollector.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svalero.fancollector.domain.enums.RolUsuario;
import com.svalero.fancollector.dto.UsuarioInDTO;
import com.svalero.fancollector.dto.UsuarioOutDTO;
import com.svalero.fancollector.dto.UsuarioPutDTO;
import com.svalero.fancollector.dto.patches.UsuarioPasswordDTO;
import com.svalero.fancollector.dto.patches.UsuarioRolDTO;
import com.svalero.fancollector.exception.domain.UsuarioNoEncontradoException;
import com.svalero.fancollector.security.jwt.JwtService;
import com.svalero.fancollector.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
@WithMockUser(username = "nerea@test.com", roles = {"ADMIN"})
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean private
    JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testListarUsuarios() throws Exception {
        UsuarioOutDTO usuario1 = new UsuarioOutDTO();
        usuario1.setId(1L);
        usuario1.setNombre("Nerea");
        usuario1.setRol(RolUsuario.USER);
        usuario1.setFechaRegistro(LocalDateTime.now());

        UsuarioOutDTO usuario2 = new UsuarioOutDTO();
        usuario2.setId(2L);
        usuario2.setNombre("Andrea");
        usuario2.setRol(RolUsuario.USER);
        usuario2.setFechaRegistro(LocalDateTime.now());

        List<UsuarioOutDTO> usuarios = List.of(usuario1, usuario2);

        when(usuarioService.listarUsuarios(eq(null), eq(null), eq(null))).thenReturn(usuarios);

        mockMvc.perform(get("/usuarios")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Nerea"))
                .andExpect(jsonPath("$[1].nombre").value("Andrea"));
    }

    @Test
    public void testBuscarUsuarioPorIdExistente() throws Exception {
        UsuarioOutDTO usuarioOutDTO = new UsuarioOutDTO();
        usuarioOutDTO.setId(1L);
        usuarioOutDTO.setNombre("Nerea");
        usuarioOutDTO.setRol(RolUsuario.USER);

        when(usuarioService.buscarUsuarioPorId(1L)).thenReturn(usuarioOutDTO);

        mockMvc.perform(get("/usuarios/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Nerea"));
    }

    @Test
    public void testBuscarUsuarioPorIdNoExiste() throws Exception {
        when(usuarioService.buscarUsuarioPorId(999L))
                .thenThrow(new UsuarioNoEncontradoException(999L));

        mockMvc.perform(get("/usuarios/999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCrearUsuarioDatosValidos() throws Exception {
        UsuarioInDTO usuarioInDTO = new UsuarioInDTO();
        usuarioInDTO.setNombre("Nerea");
        usuarioInDTO.setEmail("nerea@gmail.com");
        usuarioInDTO.setContrasena("password123");

        UsuarioOutDTO savedDto = new UsuarioOutDTO();
        savedDto.setId(1L);
        savedDto.setNombre("Nerea");
        savedDto.setRol(RolUsuario.USER);

        when(usuarioService.crearUsuarioComoAdmin(any(UsuarioInDTO.class), anyString(), anyBoolean()))
                .thenReturn(savedDto);

        mockMvc.perform(post("/usuarios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioInDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Nerea"));
    }

    @Test
    public void testCrearUsuarioBodyInvalido() throws Exception {
        mockMvc.perform(post("/usuarios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testModificarUsuarioExistente() throws Exception {
        UsuarioPutDTO usuarioPutDTO = new UsuarioPutDTO();
        usuarioPutDTO.setNombre("Nerea Modificada");
        usuarioPutDTO.setEmail("nerea@gmail.com");

        UsuarioOutDTO response = new UsuarioOutDTO();
        response.setId(1L);
        response.setNombre("Nerea Modificada");

        when(usuarioService.modificarUsuario(
                eq(1L), any(UsuarioPutDTO.class), anyString(), anyBoolean(), anyBoolean()
        )).thenReturn(response);

        mockMvc.perform(put("/usuarios/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioPutDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Nerea Modificada"));
    }

    @Test
    public void testModificarUsuarioNoExiste() throws Exception {
        UsuarioPutDTO usuarioPutDTO = new UsuarioPutDTO();
        usuarioPutDTO.setNombre("Nerea");
        usuarioPutDTO.setEmail("nerea@gmail.com");

        when(usuarioService.modificarUsuario(eq(1L), any(UsuarioPutDTO.class), anyString(), anyBoolean(), anyBoolean()
        )).thenThrow(new UsuarioNoEncontradoException(1L));

        mockMvc.perform(put("/usuarios/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioPutDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testModificarUsuarioBodyInvalido() throws Exception {
        mockMvc.perform(put("/usuarios/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testActualizarContrasena() throws Exception {
        UsuarioPasswordDTO passwordDTO = new UsuarioPasswordDTO();
        passwordDTO.setContrasena("newpassword123");

        UsuarioOutDTO response = new UsuarioOutDTO();
        response.setId(1L);
        response.setNombre("Nerea");

        when(usuarioService.actualizarContrasena(eq(1L), eq("newpassword123"), anyString(), anyBoolean(), anyBoolean()
        )).thenReturn(response);

        mockMvc.perform(patch("/usuarios/1/contrasena")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testActualizarRol() throws Exception {
        UsuarioRolDTO rolDTO = new UsuarioRolDTO();
        rolDTO.setRol(RolUsuario.MODS);

        UsuarioOutDTO response = new UsuarioOutDTO();
        response.setId(1L);
        response.setNombre("Nerea");
        response.setRol(RolUsuario.MODS);

        when(usuarioService.actualizarRol(eq(1L), eq(RolUsuario.MODS), anyString()))
                .thenReturn(response);

        mockMvc.perform(patch("/usuarios/1/rol")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rolDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rol").value("MODS"));
    }

    @Test
    public void testEliminarUsuarioExistente() throws Exception {
        mockMvc.perform(delete("/usuarios/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testEliminarUsuarioNoExiste() throws Exception {
        doThrow(new UsuarioNoEncontradoException(1L))
                .when(usuarioService).borrarUsuario(eq(1L), anyString(), anyBoolean(), anyBoolean());

        mockMvc.perform(delete("/usuarios/1")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}