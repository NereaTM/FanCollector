package com.svalero.fancollector.service;

import com.svalero.fancollector.domain.Coleccion;
import com.svalero.fancollector.domain.Item;
import com.svalero.fancollector.domain.Usuario;
import com.svalero.fancollector.domain.UsuarioItem;
import com.svalero.fancollector.domain.enums.EstadoItem;
import com.svalero.fancollector.dto.UsuarioItemInDTO;
import com.svalero.fancollector.dto.UsuarioItemOutDTO;
import com.svalero.fancollector.dto.UsuarioItemPutDTO;
import com.svalero.fancollector.exception.domain.ColeccionNoEncontradaException;
import com.svalero.fancollector.exception.domain.ItemNoEncontradoException;
import com.svalero.fancollector.exception.domain.UsuarioItemNoEncontradoException;
import com.svalero.fancollector.exception.domain.UsuarioNoEncontradoException;
import com.svalero.fancollector.exception.validation.RelacionYaExisteException;
import com.svalero.fancollector.repository.ColeccionRepository;
import com.svalero.fancollector.repository.ItemRepository;
import com.svalero.fancollector.repository.UsuarioItemRepository;
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
public class UsuarioItemServiceTest {

    @InjectMocks
    private UsuarioItemServiceImpl usuarioItemService;

    @Mock
    private UsuarioItemRepository usuarioItemRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ColeccionRepository coleccionRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testCrear() throws UsuarioNoEncontradoException, ItemNoEncontradoException, ColeccionNoEncontradaException {
        UsuarioItemInDTO dto = new UsuarioItemInDTO();
        dto.setIdUsuario(1L);
        dto.setIdItem(1L);
        dto.setIdColeccion(1L);
        dto.setEstado(EstadoItem.TENGO);
        dto.setCantidad(1);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Coleccion coleccion = new Coleccion();
        coleccion.setId(1L);

        Item item = new Item();
        item.setId(1L);
        item.setColeccion(coleccion);

        UsuarioItem uiGuardado = new UsuarioItem();
        uiGuardado.setId(1L);
        uiGuardado.setUsuario(usuario);
        uiGuardado.setColeccion(coleccion);
        uiGuardado.setItem(item);
        uiGuardado.setEstado(EstadoItem.TENGO);

        UsuarioItemOutDTO outDTO = new UsuarioItemOutDTO();
        outDTO.setId(1L);
        outDTO.setIdUsuario(1L);
        outDTO.setIdItem(1L);
        outDTO.setIdColeccion(1L);

        when(usuarioItemRepository.existsByUsuarioIdAndColeccionIdAndItemId(1L, 1L, 1L)).thenReturn(false);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(coleccionRepository.findById(1L)).thenReturn(Optional.of(coleccion));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(usuarioItemRepository.save(any(UsuarioItem.class))).thenReturn(uiGuardado);
        when(modelMapper.map(uiGuardado, UsuarioItemOutDTO.class)).thenReturn(outDTO);

        UsuarioItemOutDTO resultado = usuarioItemService.crear(dto);

        assertEquals(1L, resultado.getId());
        assertEquals(1L, resultado.getIdUsuario());
        assertEquals(1L, resultado.getIdItem());
        verify(usuarioItemRepository, times(1)).save(any(UsuarioItem.class));
    }

    @Test
    public void testCrearRelacionYaExiste() {
        UsuarioItemInDTO dto = new UsuarioItemInDTO();
        dto.setIdUsuario(1L);
        dto.setIdItem(1L);
        dto.setIdColeccion(1L);

        when(usuarioItemRepository.existsByUsuarioIdAndColeccionIdAndItemId(1L, 1L, 1L)).thenReturn(true);

        assertThrows(RelacionYaExisteException.class, () -> {
            usuarioItemService.crear(dto);
        });

        verify(usuarioItemRepository, times(0)).save(any(UsuarioItem.class));
    }

    @Test
    public void testCrearItemNoPertenece() {
        UsuarioItemInDTO dto = new UsuarioItemInDTO();
        dto.setIdUsuario(1L);
        dto.setIdItem(1L);
        dto.setIdColeccion(1L);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Coleccion coleccion = new Coleccion();
        coleccion.setId(1L);

        Coleccion otraColeccion = new Coleccion();
        otraColeccion.setId(2L);

        Item item = new Item();
        item.setId(1L);
        item.setColeccion(otraColeccion);

        when(usuarioItemRepository.existsByUsuarioIdAndColeccionIdAndItemId(1L, 1L, 1L)).thenReturn(false);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(coleccionRepository.findById(1L)).thenReturn(Optional.of(coleccion));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThrows(RuntimeException.class, () -> {
            usuarioItemService.crear(dto);
        });
    }

    @Test
    public void testBuscarPorId() throws UsuarioItemNoEncontradoException {
        UsuarioItem ui = new UsuarioItem();
        ui.setId(1L);

        UsuarioItemOutDTO outDTO = new UsuarioItemOutDTO();
        outDTO.setId(1L);

        when(usuarioItemRepository.findById(1L)).thenReturn(Optional.of(ui));
        when(modelMapper.map(ui, UsuarioItemOutDTO.class)).thenReturn(outDTO);

        UsuarioItemOutDTO resultado = usuarioItemService.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
        verify(usuarioItemRepository, times(1)).findById(1L);
    }

    @Test
    public void testBuscarPorIdNoEncontrado() {
        when(usuarioItemRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UsuarioItemNoEncontradoException.class, () -> {
            usuarioItemService.buscarPorId(999L);
        });

        verify(usuarioItemRepository, times(1)).findById(999L);
    }

    @Test
    public void testListar() {
        UsuarioItem ui1 = new UsuarioItem();
        ui1.setId(1L);

        UsuarioItem ui2 = new UsuarioItem();
        ui2.setId(2L);

        List<UsuarioItem> lista = List.of(ui1, ui2);

        UsuarioItemOutDTO dto1 = new UsuarioItemOutDTO();
        dto1.setId(1L);

        UsuarioItemOutDTO dto2 = new UsuarioItemOutDTO();
        dto2.setId(2L);

        when(usuarioItemRepository.findAll()).thenReturn(lista);
        when(modelMapper.map(ui1, UsuarioItemOutDTO.class)).thenReturn(dto1);
        when(modelMapper.map(ui2, UsuarioItemOutDTO.class)).thenReturn(dto2);

        List<UsuarioItemOutDTO> resultado = usuarioItemService.listar(null, null, null, null, null);

        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals(2L, resultado.get(1).getId());
        verify(usuarioItemRepository, times(1)).findAll();
    }

    @Test
    public void testActualizarCompleto() throws UsuarioItemNoEncontradoException {
        UsuarioItemPutDTO dto = new UsuarioItemPutDTO();
        dto.setEstado(EstadoItem.EN_CAMINO);
        dto.setEsVisible(false);
        dto.setCantidad(2);

        UsuarioItem uiExistente = new UsuarioItem();
        uiExistente.setId(1L);
        uiExistente.setEstado(EstadoItem.BUSCO);

        UsuarioItem uiActualizado = new UsuarioItem();
        uiActualizado.setId(1L);
        uiActualizado.setEstado(EstadoItem.EN_CAMINO);

        UsuarioItemOutDTO outDTO = new UsuarioItemOutDTO();
        outDTO.setId(1L);
        outDTO.setEstado(EstadoItem.EN_CAMINO);

        when(usuarioItemRepository.findById(1L)).thenReturn(Optional.of(uiExistente));
        when(usuarioItemRepository.save(any(UsuarioItem.class))).thenReturn(uiActualizado);
        when(modelMapper.map(uiActualizado, UsuarioItemOutDTO.class)).thenReturn(outDTO);

        UsuarioItemOutDTO resultado = usuarioItemService.actualizarCompleto(1L, dto);

        assertEquals(EstadoItem.EN_CAMINO, resultado.getEstado());
        verify(usuarioItemRepository, times(1)).findById(1L);
        verify(usuarioItemRepository, times(1)).save(any(UsuarioItem.class));
    }

    @Test
    public void testActualizarVisibilidad() throws UsuarioItemNoEncontradoException {
        UsuarioItem ui = new UsuarioItem();
        ui.setId(1L);
        ui.setEsVisible(true);

        UsuarioItemOutDTO outDTO = new UsuarioItemOutDTO();
        outDTO.setId(1L);
        outDTO.setEsVisible(false);

        when(usuarioItemRepository.findById(1L)).thenReturn(Optional.of(ui));
        when(usuarioItemRepository.save(any(UsuarioItem.class))).thenReturn(ui);
        when(modelMapper.map(ui, UsuarioItemOutDTO.class)).thenReturn(outDTO);

        UsuarioItemOutDTO resultado = usuarioItemService.actualizarVisibilidad(1L, false);

        assertFalse(resultado.isEsVisible());
        verify(usuarioItemRepository, times(1)).findById(1L);
        verify(usuarioItemRepository, times(1)).save(any(UsuarioItem.class));
    }

    @Test
    public void testEliminar() throws UsuarioItemNoEncontradoException {
        when(usuarioItemRepository.existsById(1L)).thenReturn(true);

        usuarioItemService.eliminar(1L);

        verify(usuarioItemRepository, times(1)).existsById(1L);
        verify(usuarioItemRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testEliminarNoEncontrado() {
        when(usuarioItemRepository.existsById(999L)).thenReturn(false);

        assertThrows(UsuarioItemNoEncontradoException.class, () -> {
            usuarioItemService.eliminar(999L);
        });

        verify(usuarioItemRepository, times(1)).existsById(999L);
        verify(usuarioItemRepository, times(0)).deleteById(anyLong());
    }

    @Test
    public void testCrearEstadoBuscoNormalizaCantidad() throws Exception {
        UsuarioItemInDTO dto = new UsuarioItemInDTO();
        dto.setIdUsuario(1L);
        dto.setIdItem(1L);
        dto.setIdColeccion(1L);
        dto.setEstado(EstadoItem.BUSCO);
        dto.setCantidad(5);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Coleccion coleccion = new Coleccion();
        coleccion.setId(1L);

        Item item = new Item();
        item.setId(1L);
        item.setColeccion(coleccion);

        UsuarioItem uiGuardado = new UsuarioItem();
        uiGuardado.setId(1L);

        UsuarioItemOutDTO outDTO = new UsuarioItemOutDTO();
        outDTO.setId(1L);
        outDTO.setCantidad(0);

        when(usuarioItemRepository.existsByUsuarioIdAndColeccionIdAndItemId(1L, 1L, 1L)).thenReturn(false);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(coleccionRepository.findById(1L)).thenReturn(Optional.of(coleccion));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(usuarioItemRepository.save(any(UsuarioItem.class))).thenReturn(uiGuardado);
        when(modelMapper.map(uiGuardado, UsuarioItemOutDTO.class)).thenReturn(outDTO);

        UsuarioItemOutDTO resultado = usuarioItemService.crear(dto);

        assertEquals(0, resultado.getCantidad());
    }
}