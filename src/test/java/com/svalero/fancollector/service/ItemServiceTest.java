package com.svalero.fancollector.service;

import com.svalero.fancollector.domain.Coleccion;
import com.svalero.fancollector.domain.Item;
import com.svalero.fancollector.domain.enums.RarezaItem;
import com.svalero.fancollector.dto.ItemInDTO;
import com.svalero.fancollector.dto.ItemOutDTO;
import com.svalero.fancollector.dto.ItemPutDTO;
import com.svalero.fancollector.exception.domain.ColeccionNoEncontradaException;
import com.svalero.fancollector.exception.domain.ItemNoEncontradoException;
import com.svalero.fancollector.repository.ColeccionRepository;
import com.svalero.fancollector.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ColeccionRepository coleccionRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testCrearItem() throws ColeccionNoEncontradaException {
        ItemInDTO itemInDTO = new ItemInDTO();
        itemInDTO.setIdColeccion(1L);
        itemInDTO.setNombre("Darkrai");
        itemInDTO.setTipo("Figura");
        itemInDTO.setRareza("LEGENDARIO");

        Coleccion coleccion = new Coleccion();
        coleccion.setId(1L);
        coleccion.setNombre("Figuras Anime");

        Item itemMapeado = new Item();
        itemMapeado.setNombre("Darkrai");
        itemMapeado.setRareza(RarezaItem.LEGENDARIO);

        Item itemGuardado = new Item();
        itemGuardado.setId(1L);
        itemGuardado.setNombre("Darkrai");
        itemGuardado.setRareza(RarezaItem.LEGENDARIO);
        itemGuardado.setColeccion(coleccion);

        ItemOutDTO itemOutDTO = new ItemOutDTO();
        itemOutDTO.setId(1L);
        itemOutDTO.setNombre("Darkrai");
        itemOutDTO.setRareza(RarezaItem.LEGENDARIO);

        when(coleccionRepository.findById(1L)).thenReturn(Optional.of(coleccion));
        when(modelMapper.map(itemInDTO, Item.class)).thenReturn(itemMapeado);
        when(itemRepository.save(any(Item.class))).thenReturn(itemGuardado);
        when(modelMapper.map(itemGuardado, ItemOutDTO.class)).thenReturn(itemOutDTO);

        ItemOutDTO resultado = itemService.crearItem(itemInDTO);

        assertEquals(1L, resultado.getId());
        assertEquals("Darkrai", resultado.getNombre());
        assertEquals(RarezaItem.LEGENDARIO, resultado.getRareza());
        verify(coleccionRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    public void testCrearItemColeccionNoEncontrada() {
        ItemInDTO itemInDTO = new ItemInDTO();
        itemInDTO.setIdColeccion(999L);

        when(coleccionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ColeccionNoEncontradaException.class, () -> {
            itemService.crearItem(itemInDTO);
        });

        verify(coleccionRepository, times(1)).findById(999L);
        verify(itemRepository, times(0)).save(any(Item.class));
    }

    @Test
    public void testBuscarItemPorId() throws ItemNoEncontradoException {
        Item item = new Item();
        item.setId(1L);
        item.setNombre("Darkrai");

        ItemOutDTO itemOutDTO = new ItemOutDTO();
        itemOutDTO.setId(1L);
        itemOutDTO.setNombre("Darkrai");

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(modelMapper.map(item, ItemOutDTO.class)).thenReturn(itemOutDTO);

        ItemOutDTO resultado = itemService.buscarItemPorId(1L);

        assertEquals(1L, resultado.getId());
        assertEquals("Darkrai", resultado.getNombre());
        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    public void testBuscarItemPorIdNoEncontrado() {
        when(itemRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ItemNoEncontradoException.class, () -> {
            itemService.buscarItemPorId(999L);
        });
        verify(itemRepository, times(1)).findById(999L);
    }

    @Test
    public void testListarItems() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setNombre("Darkrai");

        Item item2 = new Item();
        item2.setId(2L);
        item2.setNombre("Pikachu");

        List<Item> items = List.of(item1, item2);

        ItemOutDTO dto1 = new ItemOutDTO();
        dto1.setId(1L);
        dto1.setNombre("Darkrai");

        ItemOutDTO dto2 = new ItemOutDTO();
        dto2.setId(2L);
        dto2.setNombre("Pikachu");

        when(itemRepository.findAll()).thenReturn(items);
        when(modelMapper.map(item1, ItemOutDTO.class)).thenReturn(dto1);
        when(modelMapper.map(item2, ItemOutDTO.class)).thenReturn(dto2);

        List<ItemOutDTO> resultado = itemService.listarItems(null, null, null, null);

        assertEquals(2, resultado.size());
        assertEquals("Darkrai", resultado.get(0).getNombre());
        assertEquals("Pikachu", resultado.get(1).getNombre());
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    public void testActualizarItem() throws ItemNoEncontradoException {
        ItemPutDTO itemPutDTO = new ItemPutDTO();
        itemPutDTO.setNombre("Darkrai");
        itemPutDTO.setRareza("EPICO");

        Item itemExistente = new Item();
        itemExistente.setId(1L);
        itemExistente.setNombre("Darkrai");

        Item itemActualizado = new Item();
        itemActualizado.setId(1L);
        itemActualizado.setNombre("Darkrai Actualizado");
        itemActualizado.setRareza(RarezaItem.EPICO);

        ItemOutDTO itemOutDTO = new ItemOutDTO();
        itemOutDTO.setId(1L);
        itemOutDTO.setNombre("Darkrai Actualizado");
        itemOutDTO.setRareza(RarezaItem.EPICO);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(itemExistente));
        when(itemRepository.save(any(Item.class))).thenReturn(itemActualizado);
        when(modelMapper.map(itemActualizado, ItemOutDTO.class)).thenReturn(itemOutDTO);

        ItemOutDTO resultado = itemService.actualizarItem(1L, itemPutDTO);

        assertEquals("Darkrai Actualizado", resultado.getNombre());
        assertEquals(RarezaItem.EPICO, resultado.getRareza());
        verify(itemRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    public void testActualizarRareza() throws ItemNoEncontradoException {
        Item item = new Item();
        item.setId(1L);
        item.setRareza(RarezaItem.COMUN);

        ItemOutDTO itemOutDTO = new ItemOutDTO();
        itemOutDTO.setId(1L);
        itemOutDTO.setRareza(RarezaItem.LEGENDARIO);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(modelMapper.map(item, ItemOutDTO.class)).thenReturn(itemOutDTO);

        ItemOutDTO resultado = itemService.actualizarRareza(1L, RarezaItem.LEGENDARIO);

        assertEquals(RarezaItem.LEGENDARIO, resultado.getRareza());
        verify(itemRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    public void testEliminarItem() throws ItemNoEncontradoException {
        Item item = new Item();
        item.setId(1L);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        itemService.eliminarItem(1L);

        verify(itemRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).delete(item);
    }

    @Test
    public void testEliminarItemNoEncontrado() {
        when(itemRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ItemNoEncontradoException.class, () -> {
            itemService.eliminarItem(999L);
        });

        verify(itemRepository, times(1)).findById(999L);
        verify(itemRepository, times(0)).delete(any(Item.class));
    }

    @Test
    public void testCrearItemRarezaInvalida() {
        ItemInDTO itemInDTO = new ItemInDTO();
        itemInDTO.setIdColeccion(1L);
        itemInDTO.setNombre("Darkrai");
        itemInDTO.setRareza("INVALIDO");

        Coleccion coleccion = new Coleccion();
        coleccion.setId(1L);

        Item item = new Item();

        when(coleccionRepository.findById(1L)).thenReturn(Optional.of(coleccion));
        when(modelMapper.map(itemInDTO, Item.class)).thenReturn(item);

        assertThrows(IllegalArgumentException.class, () -> {
            itemService.crearItem(itemInDTO);
        });
    }
}