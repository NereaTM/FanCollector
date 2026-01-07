package com.svalero.fancollector.service;

import com.svalero.fancollector.domain.Coleccion;
import com.svalero.fancollector.domain.Usuario;
import com.svalero.fancollector.dto.ColeccionInDTO;
import com.svalero.fancollector.dto.ColeccionOutDTO;
import com.svalero.fancollector.dto.ColeccionPutDTO;
import com.svalero.fancollector.exception.domain.ColeccionNoEncontradaException;
import com.svalero.fancollector.exception.domain.UsuarioNoEncontradoException;
import com.svalero.fancollector.repository.ColeccionRepository;
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
public class ColeccionServiceTest {

    @InjectMocks
    private ColeccionServiceImpl coleccionService;

    @Mock
    private ColeccionRepository coleccionRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testCrearColeccion() throws UsuarioNoEncontradoException {
        ColeccionInDTO coleccionInDTO = new ColeccionInDTO();
        coleccionInDTO.setIdCreador(1L);
        coleccionInDTO.setNombre("Figuras Anime");
        coleccionInDTO.setCategoria("Anime");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Nerea");

        Coleccion coleccionMapeada = new Coleccion();
        coleccionMapeada.setNombre("Figuras Anime");

        Coleccion coleccionGuardada = new Coleccion();
        coleccionGuardada.setId(1L);
        coleccionGuardada.setNombre("Figuras Anime");
        coleccionGuardada.setCreador(usuario);

        ColeccionOutDTO coleccionOutDTO = new ColeccionOutDTO();
        coleccionOutDTO.setId(1L);
        coleccionOutDTO.setNombre("Figuras Anime");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(modelMapper.map(coleccionInDTO, Coleccion.class)).thenReturn(coleccionMapeada);
        when(coleccionRepository.save(any(Coleccion.class))).thenReturn(coleccionGuardada);
        when(modelMapper.map(coleccionGuardada, ColeccionOutDTO.class)).thenReturn(coleccionOutDTO);

        ColeccionOutDTO resultado = coleccionService.crearColeccion(coleccionInDTO);

        assertEquals(1L, resultado.getId());
        assertEquals("Figuras Anime", resultado.getNombre());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(coleccionRepository, times(1)).save(any(Coleccion.class));
    }

    @Test
    public void testCrearColeccionUsuarioNoEncontrado() {
        ColeccionInDTO coleccionInDTO = new ColeccionInDTO();
        coleccionInDTO.setIdCreador(999L);

        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class, () -> {
            coleccionService.crearColeccion(coleccionInDTO);
        });

        verify(usuarioRepository, times(1)).findById(999L);
        verify(coleccionRepository, times(0)).save(any(Coleccion.class));
    }

    @Test
    public void testBuscarColeccionPorId() throws ColeccionNoEncontradaException {
        Coleccion coleccion = new Coleccion();
        coleccion.setId(1L);
        coleccion.setNombre("Figuras Anime");

        ColeccionOutDTO coleccionOutDTO = new ColeccionOutDTO();
        coleccionOutDTO.setId(1L);
        coleccionOutDTO.setNombre("Figuras Anime");

        when(coleccionRepository.findById(1L)).thenReturn(Optional.of(coleccion));
        when(modelMapper.map(coleccion, ColeccionOutDTO.class)).thenReturn(coleccionOutDTO);

        ColeccionOutDTO resultado = coleccionService.buscarColeccionPorId(1L);

        assertEquals(1L, resultado.getId());
        assertEquals("Figuras Anime", resultado.getNombre());
        verify(coleccionRepository, times(1)).findById(1L);
    }

    @Test
    public void testBuscarColeccionPorIdNoEncontrada() {
        when(coleccionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ColeccionNoEncontradaException.class, () -> {
            coleccionService.buscarColeccionPorId(999L);
        });

        verify(coleccionRepository, times(1)).findById(999L);
    }

    @Test
    public void testListarColecciones() {
        Coleccion coleccion1 = new Coleccion();
        coleccion1.setId(1L);
        coleccion1.setNombre("Figuras Anime");

        Coleccion coleccion2 = new Coleccion();
        coleccion2.setId(2L);
        coleccion2.setNombre("Trading Cards");

        List<Coleccion> colecciones = List.of(coleccion1, coleccion2);

        ColeccionOutDTO dto1 = new ColeccionOutDTO();
        dto1.setId(1L);
        dto1.setNombre("Figuras Anime");

        ColeccionOutDTO dto2 = new ColeccionOutDTO();
        dto2.setId(2L);
        dto2.setNombre("Trading Cards");

        when(coleccionRepository.findAll()).thenReturn(colecciones);
        when(modelMapper.map(coleccion1, ColeccionOutDTO.class)).thenReturn(dto1);
        when(modelMapper.map(coleccion2, ColeccionOutDTO.class)).thenReturn(dto2);

        List<ColeccionOutDTO> resultado = coleccionService.listarColecciones(null, null, null, null);

        assertEquals(2, resultado.size());
        assertEquals("Figuras Anime", resultado.get(0).getNombre());
        assertEquals("Trading Cards", resultado.get(1).getNombre());
        verify(coleccionRepository, times(1)).findAll();
    }

    @Test
    public void testActualizarColeccion() throws ColeccionNoEncontradaException {
        ColeccionPutDTO coleccionPutDTO = new ColeccionPutDTO();
        coleccionPutDTO.setNombre("Figuras Anime Actualizado");
        coleccionPutDTO.setCategoria("Anime");

        coleccionPutDTO.setEsPublica(false);
        coleccionPutDTO.setUsableComoPlantilla(false);

        Coleccion coleccionExistente = new Coleccion();
        coleccionExistente.setId(1L);
        coleccionExistente.setNombre("Figuras Anime");

        Coleccion coleccionActualizada = new Coleccion();
        coleccionActualizada.setId(1L);
        coleccionActualizada.setNombre("Figuras Anime Actualizado");

        ColeccionOutDTO coleccionOutDTO = new ColeccionOutDTO();
        coleccionOutDTO.setId(1L);
        coleccionOutDTO.setNombre("Figuras Anime Actualizado");

        when(coleccionRepository.findById(1L)).thenReturn(Optional.of(coleccionExistente));
        when(coleccionRepository.save(any(Coleccion.class))).thenReturn(coleccionActualizada);
        when(modelMapper.map(coleccionActualizada, ColeccionOutDTO.class)).thenReturn(coleccionOutDTO);

        ColeccionOutDTO resultado = coleccionService.actualizarColeccion(1L, coleccionPutDTO);

        assertEquals("Figuras Anime Actualizado", resultado.getNombre());
        verify(coleccionRepository, times(1)).findById(1L);
        verify(coleccionRepository, times(1)).save(any(Coleccion.class));
    }

    @Test
    public void testActualizarEsPublica() throws ColeccionNoEncontradaException {
        Coleccion coleccion = new Coleccion();
        coleccion.setId(1L);
        coleccion.setNombre("Figuras Anime");
        coleccion.setEsPublica(false);

        ColeccionOutDTO coleccionOutDTO = new ColeccionOutDTO();
        coleccionOutDTO.setId(1L);
        coleccionOutDTO.setEsPublica(true);

        when(coleccionRepository.findById(1L)).thenReturn(Optional.of(coleccion));
        when(coleccionRepository.save(any(Coleccion.class))).thenReturn(coleccion);
        when(modelMapper.map(coleccion, ColeccionOutDTO.class)).thenReturn(coleccionOutDTO);

        ColeccionOutDTO resultado = coleccionService.actualizarEsPublica(1L, true);

        assertTrue(resultado.isEsPublica());
        verify(coleccionRepository, times(1)).findById(1L);
        verify(coleccionRepository, times(1)).save(any(Coleccion.class));
    }

    @Test
    public void testActualizarUsableComoPlantilla() throws ColeccionNoEncontradaException {
        Coleccion coleccion = new Coleccion();
        coleccion.setId(1L);
        coleccion.setUsableComoPlantilla(false);

        ColeccionOutDTO coleccionOutDTO = new ColeccionOutDTO();
        coleccionOutDTO.setId(1L);
        coleccionOutDTO.setUsableComoPlantilla(true);

        when(coleccionRepository.findById(1L)).thenReturn(Optional.of(coleccion));
        when(coleccionRepository.save(any(Coleccion.class))).thenReturn(coleccion);
        when(modelMapper.map(coleccion, ColeccionOutDTO.class)).thenReturn(coleccionOutDTO);

        ColeccionOutDTO resultado = coleccionService.actualizarUsableComoPlantilla(1L, true);

        assertTrue(resultado.isUsableComoPlantilla());
        verify(coleccionRepository, times(1)).findById(1L);
        verify(coleccionRepository, times(1)).save(any(Coleccion.class));
    }

    @Test
    public void testEliminarColeccion() throws ColeccionNoEncontradaException {
        Coleccion coleccion = new Coleccion();
        coleccion.setId(1L);

        when(coleccionRepository.findById(1L)).thenReturn(Optional.of(coleccion));

        coleccionService.eliminarColeccion(1L);

        verify(coleccionRepository, times(1)).findById(1L);
        verify(coleccionRepository, times(1)).delete(coleccion);
    }

    @Test
    public void testEliminarColeccionNoEncontrada() {
        when(coleccionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ColeccionNoEncontradaException.class, () -> {
            coleccionService.eliminarColeccion(999L);
        });

        verify(coleccionRepository, times(1)).findById(999L);
        verify(coleccionRepository, times(0)).delete(any(Coleccion.class));
    }
}