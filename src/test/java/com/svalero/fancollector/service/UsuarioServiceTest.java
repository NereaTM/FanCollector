package com.svalero.fancollector.service;

import com.svalero.fancollector.domain.Usuario;
import com.svalero.fancollector.domain.enums.RolUsuario;
import com.svalero.fancollector.dto.UsuarioInDTO;
import com.svalero.fancollector.dto.UsuarioOutDTO;
import com.svalero.fancollector.exception.domain.UsuarioNoEncontradoException;
import com.svalero.fancollector.exception.validation.EmailDuplicadoException;
import com.svalero.fancollector.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testCrearUsuario() {
        UsuarioInDTO usuarioInDTO = new UsuarioInDTO();
        usuarioInDTO.setNombre("Nerea");
        usuarioInDTO.setEmail("nerea@gmail.com");
        usuarioInDTO.setContrasena("password123");

        Usuario usuarioMapeado = new Usuario();
        usuarioMapeado.setNombre("Nerea");
        usuarioMapeado.setEmail("nerea@gmail.com");

        UsuarioOutDTO usuarioOutDTO = new UsuarioOutDTO();
        usuarioOutDTO.setId(1L);
        usuarioOutDTO.setNombre("Nerea");
        usuarioOutDTO.setEmail("nerea@gmail.com");
        usuarioOutDTO.setRol(RolUsuario.USER);

        when(usuarioRepository.existsByEmail("nerea@gmail.com")).thenReturn(false);
        when(modelMapper.map(usuarioInDTO, Usuario.class)).thenReturn(usuarioMapeado);
        when(modelMapper.map(usuarioMapeado, UsuarioOutDTO.class)).thenReturn(usuarioOutDTO);

        UsuarioOutDTO resultado = usuarioService.crearUsuario(usuarioInDTO);

        assertEquals(1L, resultado.getId());
        assertEquals("Nerea", resultado.getNombre());
        assertEquals("nerea@gmail.com", resultado.getEmail());
        assertEquals(RolUsuario.USER, usuarioMapeado.getRol());
        verify(usuarioRepository).save(usuarioMapeado);
    }

    @Test
    public void testCrearUsuarioEmailDuplicado() {
        UsuarioInDTO usuarioInDTO = new UsuarioInDTO();
        usuarioInDTO.setEmail("nerea@gmail.com");

        when(usuarioRepository.existsByEmail("nerea@gmail.com")).thenReturn(true);

        assertThrows(EmailDuplicadoException.class, () -> {
            usuarioService.crearUsuario(usuarioInDTO);
        });

        verify(usuarioRepository, times(1)).existsByEmail("nerea@gmail.com");
        verify(usuarioRepository, times(0)).save(any(Usuario.class));
    }

    @Test
    public void testBuscarUsuarioPorId() throws UsuarioNoEncontradoException {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Nerea");

        UsuarioOutDTO usuarioOutDTO = new UsuarioOutDTO();
        usuarioOutDTO.setId(1L);
        usuarioOutDTO.setNombre("Nerea");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(modelMapper.map(usuario, UsuarioOutDTO.class)).thenReturn(usuarioOutDTO);

        UsuarioOutDTO resultado = usuarioService.buscarUsuarioPorId(1L);

        assertEquals(1L, resultado.getId());
        assertEquals("Nerea", resultado.getNombre());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    public void testBuscarUsuarioPorIdNoEncontrado() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class, () -> {
            usuarioService.buscarUsuarioPorId(999L);
        });

        verify(usuarioRepository, times(1)).findById(999L);
    }

    @Test
    public void testListarUsuarios() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNombre("Nerea");

        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNombre("Andrea");

        List<Usuario> usuarios = List.of(usuario1, usuario2);

        UsuarioOutDTO dto1 = new UsuarioOutDTO();
        dto1.setId(1L);
        dto1.setNombre("Nerea");

        UsuarioOutDTO dto2 = new UsuarioOutDTO();
        dto2.setId(2L);
        dto2.setNombre("Andrea");

        when(usuarioRepository.findAll()).thenReturn(usuarios);
        when(modelMapper.map(usuario1, UsuarioOutDTO.class)).thenReturn(dto1);
        when(modelMapper.map(usuario2, UsuarioOutDTO.class)).thenReturn(dto2);

        List<UsuarioOutDTO> resultado = usuarioService.listarUsuarios(null, null, null);

        assertEquals(2, resultado.size());
        assertEquals("Nerea", resultado.get(0).getNombre());
        assertEquals("Andrea", resultado.get(1).getNombre());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    public void testModificarUsuario() throws UsuarioNoEncontradoException {
        UsuarioInDTO usuarioInDTO = new UsuarioInDTO();
        usuarioInDTO.setNombre("Nerea Modificada");
        usuarioInDTO.setEmail("nerea@gmail.com");
        usuarioInDTO.setContrasena("newpassword");

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1L);
        usuarioExistente.setNombre("Nerea");

        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setId(1L);
        usuarioActualizado.setNombre("Nerea Modificada");

        UsuarioOutDTO usuarioOutDTO = new UsuarioOutDTO();
        usuarioOutDTO.setId(1L);
        usuarioOutDTO.setNombre("Nerea Modificada");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioActualizado);
        when(modelMapper.map(eq(usuarioExistente), eq(UsuarioOutDTO.class))).thenReturn(usuarioOutDTO);

        UsuarioOutDTO resultado = usuarioService.modificarUsuario(1L, usuarioInDTO);

        assertEquals("Nerea Modificada", resultado.getNombre());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    public void testModificarUsuarioNoEncontrado() {
        UsuarioInDTO usuarioInDTO = new UsuarioInDTO();

        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class, () -> {
            usuarioService.modificarUsuario(999L, usuarioInDTO);
        });

        verify(usuarioRepository, times(1)).findById(999L);
        verify(usuarioRepository, times(0)).save(any(Usuario.class));
    }

    @Test
    public void testActualizarContrasena() throws UsuarioNoEncontradoException {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setContrasena("oldpassword");

        UsuarioOutDTO usuarioOutDTO = new UsuarioOutDTO();
        usuarioOutDTO.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(modelMapper.map(usuario, UsuarioOutDTO.class)).thenReturn(usuarioOutDTO);

        UsuarioOutDTO resultado = usuarioService.actualizarContrasena(1L, "newpassword");

        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    public void testBorrarUsuario() throws UsuarioNoEncontradoException {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        usuarioService.borrarUsuario(1L);

        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).delete(usuario);
    }

    @Test
    public void testBorrarUsuarioNoEncontrado() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class, () -> {
            usuarioService.borrarUsuario(999L);
        });

        verify(usuarioRepository, times(1)).findById(999L);
        verify(usuarioRepository, times(0)).delete(any(Usuario.class));
    }
}