package com.svalero.fancollector.service;

import com.svalero.fancollector.domain.Usuario;
import com.svalero.fancollector.domain.enums.RolUsuario;
import com.svalero.fancollector.dto.UsuarioInDTO;
import com.svalero.fancollector.dto.UsuarioOutDTO;
import com.svalero.fancollector.dto.UsuarioPutDTO;
import com.svalero.fancollector.exception.domain.UsuarioNoEncontradoException;
import com.svalero.fancollector.exception.validation.EmailDuplicadoException;
import com.svalero.fancollector.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    private static final String EMAIL = "nerea@test.com";
    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void testCrearUsuario() {
        UsuarioInDTO usuarioInDTO = new UsuarioInDTO();
        usuarioInDTO.setNombre("Nerea");
        usuarioInDTO.setEmail(EMAIL);
        usuarioInDTO.setContrasena("123456");

        Usuario usuarioMapeado = new Usuario();
        usuarioMapeado.setNombre("Nerea");
        usuarioMapeado.setEmail(EMAIL);

        Usuario guardado = new Usuario();
        guardado.setId(1L);
        guardado.setNombre("Nerea");
        guardado.setEmail(EMAIL);
        guardado.setRol(RolUsuario.USER);
        guardado.setContrasena("ENC(123456)");

        UsuarioOutDTO usuarioOutDTO = new UsuarioOutDTO();
        usuarioOutDTO.setId(1L);
        usuarioOutDTO.setNombre("Nerea");
        usuarioOutDTO.setRol(RolUsuario.ADMIN);
        guardado.setContrasena("ENC(123456)");

        when(usuarioRepository.existsByEmail(EMAIL)).thenReturn(false);
        when(modelMapper.map(any(UsuarioInDTO.class), eq(Usuario.class))).thenReturn(usuarioMapeado);
        when(passwordEncoder.encode("123456")).thenReturn("ENC(123456)");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(guardado);
        when(modelMapper.map(guardado, UsuarioOutDTO.class)).thenReturn(usuarioOutDTO);

        UsuarioOutDTO resultado = usuarioService.crearUsuario(usuarioInDTO);

        assertEquals(1L, resultado.getId());
        assertEquals("Nerea", resultado.getNombre());
        assertEquals(RolUsuario.ADMIN, resultado.getRol());

        verify(usuarioRepository).existsByEmail(EMAIL);
        verify(passwordEncoder).encode("123456");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    public void testCrearUsuarioEmailDuplicado() {
        UsuarioInDTO usuarioInDTO = new UsuarioInDTO();
        usuarioInDTO.setEmail("EMAIL");

        when(usuarioRepository.existsByEmail("EMAIL")).thenReturn(true);

        assertThrows(EmailDuplicadoException.class, () -> {
            usuarioService.crearUsuario(usuarioInDTO);
        });

        verify(usuarioRepository, times(1)).existsByEmail("EMAIL");
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
        final String EMAIL = "EMAIL";
        boolean esAdmin = false;
        boolean esMods = false;

        UsuarioPutDTO dto = new UsuarioPutDTO();
        dto.setNombre("Nerea Modificada");
        dto.setEmail(EMAIL);

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1L);
        usuarioExistente.setNombre("Nerea");
        usuarioExistente.setEmail(EMAIL);

        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setId(1L);
        usuarioActualizado.setNombre("Nerea Modificada");
        usuarioExistente.setEmail(EMAIL);

        UsuarioOutDTO usuarioOutDTO = new UsuarioOutDTO();
        usuarioOutDTO.setId(1L);
        usuarioOutDTO.setNombre("Nerea Modificada");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioActualizado);
        when(modelMapper.map(any(Usuario.class), eq(UsuarioOutDTO.class))).thenReturn(usuarioOutDTO);

        UsuarioOutDTO resultado = usuarioService.modificarUsuario(1L, dto, EMAIL, esAdmin, esMods);

        assertEquals("Nerea Modificada", resultado.getNombre());
        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    public void testModificarUsuarioNoEncontrado() {
        final String EMAIL = "EMAIL";
        boolean esAdmin = false;
        boolean esMods = false;

        UsuarioPutDTO dto = new UsuarioPutDTO();
        dto.setNombre("Nerea");
        dto.setEmail(EMAIL);

        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class, () -> {
            usuarioService.modificarUsuario(999L, dto, EMAIL, esAdmin, esMods);
        });

        verify(usuarioRepository, times(1)).findById(999L);
        verify(usuarioRepository, times(0)).save(any(Usuario.class));
    }

    @Test
    public void testActualizarContrasena() throws UsuarioNoEncontradoException {
        final String EMAIL = "EMAIL";
        boolean esAdmin = false;
        boolean esMods = false;

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail(EMAIL);
        usuario.setContrasena("123456");

        Usuario guardado = new Usuario();
        guardado.setEmail(EMAIL);
        guardado.setContrasena("123456");

        UsuarioOutDTO out = new UsuarioOutDTO();
        out.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("newpassword")).thenReturn("ENC(newpassword)");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(guardado);
        when(modelMapper.map(guardado, UsuarioOutDTO.class)).thenReturn(out);

        UsuarioOutDTO resultado = usuarioService.actualizarContrasena(1L, "newpassword", EMAIL, esAdmin, esMods);
        assertNotNull(resultado);

        verify(usuarioRepository).findById(1L);
        verify(passwordEncoder).encode("newpassword");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    public void testBorrarUsuario() throws UsuarioNoEncontradoException {
        final String EMAIL = "EMAIL";
        boolean esAdmin = false;
        boolean esMods = false;

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail(EMAIL);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        usuarioService.borrarUsuario(1L, EMAIL, esAdmin, esMods);

        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).delete(usuario);
    }

    @Test
    public void testBorrarUsuarioNoEncontrado() {
        final String EMAIL = "EMAIL";
        boolean esAdmin = false;
        boolean esMods = false;

        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class, () -> {
            usuarioService.borrarUsuario(999L, EMAIL, esAdmin, esMods);
        });

        verify(usuarioRepository).findById(999L);
        verify(usuarioRepository, never()).delete(any(Usuario.class));
    }
}