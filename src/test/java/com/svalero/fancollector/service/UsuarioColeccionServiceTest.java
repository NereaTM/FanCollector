package com.svalero.fancollector.service;

import com.svalero.fancollector.domain.Coleccion;
import com.svalero.fancollector.domain.Usuario;
import com.svalero.fancollector.domain.UsuarioColeccion;
import com.svalero.fancollector.dto.UsuarioColeccionInDTO;
import com.svalero.fancollector.dto.UsuarioColeccionOutDTO;
import com.svalero.fancollector.dto.UsuarioColeccionPutDTO;
import com.svalero.fancollector.dto.patches.UsuarioColeccionFavoritaDTO;
import com.svalero.fancollector.exception.domain.ColeccionNoEncontradaException;
import com.svalero.fancollector.exception.domain.UsuarioColeccionNoEncontradoException;
import com.svalero.fancollector.exception.domain.UsuarioNoEncontradoException;
import com.svalero.fancollector.exception.validation.RelacionYaExisteException;
import com.svalero.fancollector.repository.ColeccionRepository;
import com.svalero.fancollector.repository.UsuarioColeccionRepository;
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
public class UsuarioColeccionServiceTest {

    @InjectMocks
    private UsuarioColeccionServiceImpl usuarioColeccionService;

    @Mock
    private UsuarioColeccionRepository usuarioColeccionRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ColeccionRepository coleccionRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testCrear() throws UsuarioNoEncontradoException, ColeccionNoEncontradaException {
        UsuarioColeccionInDTO dto = new UsuarioColeccionInDTO();
        dto.setIdUsuario(1L);
        dto.setIdColeccion(1L);
        dto.setEsFavorita(false);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Coleccion coleccion = new Coleccion();
        coleccion.setId(1L);

        UsuarioColeccion ucMapeado = new UsuarioColeccion();
        ucMapeado.setEsFavorita(false);

        UsuarioColeccion ucGuardado = new UsuarioColeccion();
        ucGuardado.setId(1L);
        ucGuardado.setUsuario(usuario);
        ucGuardado.setColeccion(coleccion);

        UsuarioColeccionOutDTO outDTO = new UsuarioColeccionOutDTO();
        outDTO.setId(1L);
        outDTO.setIdUsuario(1L);
        outDTO.setIdColeccion(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(coleccionRepository.findById(1L)).thenReturn(Optional.of(coleccion));
        when(usuarioColeccionRepository.existsByUsuario_IdAndColeccion_Id(1L, 1L)).thenReturn(false);
        when(modelMapper.map(dto, UsuarioColeccion.class)).thenReturn(ucMapeado);
        when(usuarioColeccionRepository.save(any(UsuarioColeccion.class))).thenReturn(ucGuardado);
        when(modelMapper.map(ucGuardado, UsuarioColeccionOutDTO.class)).thenReturn(outDTO);

        UsuarioColeccionOutDTO resultado = usuarioColeccionService.crear(dto);

        assertEquals(1L, resultado.getId());
        assertEquals(1L, resultado.getIdUsuario());
        assertEquals(1L, resultado.getIdColeccion());
        verify(usuarioColeccionRepository, times(1)).save(any(UsuarioColeccion.class));
    }

    @Test
    public void testCrearRelacionYaExiste() {
        UsuarioColeccionInDTO dto = new UsuarioColeccionInDTO();
        dto.setIdUsuario(1L);
        dto.setIdColeccion(1L);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Coleccion coleccion = new Coleccion();
        coleccion.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(coleccionRepository.findById(1L)).thenReturn(Optional.of(coleccion));
        when(usuarioColeccionRepository.existsByUsuario_IdAndColeccion_Id(1L, 1L)).thenReturn(true);

        assertThrows(RelacionYaExisteException.class, () -> {
            usuarioColeccionService.crear(dto);
        });

        verify(usuarioColeccionRepository, times(0)).save(any(UsuarioColeccion.class));
    }

    @Test
    public void testBuscarPorId() throws UsuarioColeccionNoEncontradoException {
        UsuarioColeccion uc = new UsuarioColeccion();
        uc.setId(1L);

        UsuarioColeccionOutDTO outDTO = new UsuarioColeccionOutDTO();
        outDTO.setId(1L);

        when(usuarioColeccionRepository.findById(1L)).thenReturn(Optional.of(uc));
        when(modelMapper.map(uc, UsuarioColeccionOutDTO.class)).thenReturn(outDTO);

        UsuarioColeccionOutDTO resultado = usuarioColeccionService.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
        verify(usuarioColeccionRepository, times(1)).findById(1L);
    }

    @Test
    public void testBuscarPorIdNoEncontrado() {
        when(usuarioColeccionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UsuarioColeccionNoEncontradoException.class, () -> {
            usuarioColeccionService.buscarPorId(999L);
        });

        verify(usuarioColeccionRepository, times(1)).findById(999L);
    }

    @Test
    public void testListar() {
        UsuarioColeccion uc1 = new UsuarioColeccion();
        uc1.setId(1L);

        UsuarioColeccion uc2 = new UsuarioColeccion();
        uc2.setId(2L);

        List<UsuarioColeccion> lista = List.of(uc1, uc2);

        UsuarioColeccionOutDTO dto1 = new UsuarioColeccionOutDTO();
        dto1.setId(1L);

        UsuarioColeccionOutDTO dto2 = new UsuarioColeccionOutDTO();
        dto2.setId(2L);

        when(usuarioColeccionRepository.findAll()).thenReturn(lista);
        when(modelMapper.map(uc1, UsuarioColeccionOutDTO.class)).thenReturn(dto1);
        when(modelMapper.map(uc2, UsuarioColeccionOutDTO.class)).thenReturn(dto2);

        List<UsuarioColeccionOutDTO> resultado = usuarioColeccionService.listar(null, null, null);

        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals(2L, resultado.get(1).getId());
        verify(usuarioColeccionRepository, times(1)).findAll();
    }

    @Test
    public void testActualizar() throws UsuarioColeccionNoEncontradoException {
        UsuarioColeccionPutDTO dto = new UsuarioColeccionPutDTO();
        dto.setEsFavorita(true);
        dto.setEsCreador(true);

        UsuarioColeccion ucExistente = new UsuarioColeccion();
        ucExistente.setId(1L);
        ucExistente.setEsFavorita(false);

        UsuarioColeccion ucActualizado = new UsuarioColeccion();
        ucActualizado.setId(1L);
        ucActualizado.setEsFavorita(true);

        UsuarioColeccionOutDTO outDTO = new UsuarioColeccionOutDTO();
        outDTO.setId(1L);
        outDTO.setEsFavorita(true);

        when(usuarioColeccionRepository.findById(1L)).thenReturn(Optional.of(ucExistente));
        when(usuarioColeccionRepository.save(any(UsuarioColeccion.class))).thenReturn(ucActualizado);
        when(modelMapper.map(ucActualizado, UsuarioColeccionOutDTO.class)).thenReturn(outDTO);

        UsuarioColeccionOutDTO resultado = usuarioColeccionService.actualizar(1L, dto);

        assertTrue(resultado.getEsFavorita());
        verify(usuarioColeccionRepository, times(1)).findById(1L);
        verify(usuarioColeccionRepository, times(1)).save(any(UsuarioColeccion.class));
    }

    @Test
    public void testActualizarFavorita() throws UsuarioColeccionNoEncontradoException {
        UsuarioColeccionFavoritaDTO dto = new UsuarioColeccionFavoritaDTO();
        dto.setEsFavorita(true);

        UsuarioColeccion uc = new UsuarioColeccion();
        uc.setId(1L);
        uc.setEsFavorita(false);

        UsuarioColeccionOutDTO outDTO = new UsuarioColeccionOutDTO();
        outDTO.setId(1L);
        outDTO.setEsFavorita(true);

        when(usuarioColeccionRepository.findById(1L)).thenReturn(Optional.of(uc));
        when(usuarioColeccionRepository.save(any(UsuarioColeccion.class))).thenReturn(uc);
        when(modelMapper.map(uc, UsuarioColeccionOutDTO.class)).thenReturn(outDTO);

        UsuarioColeccionOutDTO resultado = usuarioColeccionService.actualizarFavorita(1L, dto);

        assertTrue(resultado.getEsFavorita());
        verify(usuarioColeccionRepository, times(1)).findById(1L);
        verify(usuarioColeccionRepository, times(1)).save(any(UsuarioColeccion.class));
    }

    @Test
    public void testEliminar() throws UsuarioColeccionNoEncontradoException {
        UsuarioColeccion uc = new UsuarioColeccion();
        uc.setId(1L);

        when(usuarioColeccionRepository.findById(1L)).thenReturn(Optional.of(uc));

        usuarioColeccionService.eliminar(1L);

        verify(usuarioColeccionRepository, times(1)).findById(1L);
        verify(usuarioColeccionRepository, times(1)).delete(uc);
    }

    @Test
    public void testEliminarNoEncontrado() {
        when(usuarioColeccionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UsuarioColeccionNoEncontradoException.class, () -> {
            usuarioColeccionService.eliminar(999L);
        });

        verify(usuarioColeccionRepository, times(1)).findById(999L);
        verify(usuarioColeccionRepository, times(0)).delete(any(UsuarioColeccion.class));
    }
}