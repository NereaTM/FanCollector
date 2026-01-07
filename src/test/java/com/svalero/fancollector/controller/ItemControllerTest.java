package com.svalero.fancollector.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svalero.fancollector.domain.enums.RarezaItem;
import com.svalero.fancollector.dto.ItemInDTO;
import com.svalero.fancollector.dto.ItemOutDTO;
import com.svalero.fancollector.dto.ItemPutDTO;
import com.svalero.fancollector.dto.patches.ItemRarezaDTO;
import com.svalero.fancollector.exception.domain.ColeccionNoEncontradaException;
import com.svalero.fancollector.exception.domain.ItemNoEncontradoException;
import com.svalero.fancollector.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testListarItems() throws Exception {
        ItemOutDTO item1 = new ItemOutDTO();
        item1.setId(1L);
        item1.setNombre("Darkrai");
        item1.setTipo("Figura");
        item1.setRareza(RarezaItem.LEGENDARIO);

        ItemOutDTO item2 = new ItemOutDTO();
        item2.setId(2L);
        item2.setNombre("Pikachu");
        item2.setTipo("Carta");
        item2.setRareza(RarezaItem.RARO);

        List<ItemOutDTO> items = List.of(item1, item2);

        when(itemService.listarItems(null, null, null, null)).thenReturn(items);

        mockMvc.perform(get("/items")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Darkrai"))
                .andExpect(jsonPath("$[1].nombre").value("Pikachu"));
    }

    @Test
    public void testBuscarItemPorIdExistente() throws Exception {
        ItemOutDTO itemOutDTO = new ItemOutDTO();
        itemOutDTO.setId(1L);
        itemOutDTO.setNombre("Darkrai");
        itemOutDTO.setRareza(RarezaItem.LEGENDARIO);

        when(itemService.buscarItemPorId(1L)).thenReturn(itemOutDTO);

        mockMvc.perform(get("/items/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Darkrai"));
    }

    @Test
    public void testBuscarItemPorIdNoExiste() throws Exception {
        when(itemService.buscarItemPorId(999L))
                .thenThrow(new ItemNoEncontradoException(999L));

        mockMvc.perform(get("/items/999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCrearItemDatosValidos() throws Exception {
        ItemInDTO itemInDTO = new ItemInDTO();
        itemInDTO.setIdColeccion(1L);
        itemInDTO.setNombre("Darkrai");
        itemInDTO.setTipo("Figura");
        itemInDTO.setRareza("LEGENDARIO");

        ItemOutDTO savedDto = new ItemOutDTO();
        savedDto.setId(1L);
        savedDto.setNombre("Darkrai");
        savedDto.setRareza(RarezaItem.LEGENDARIO);

        when(itemService.crearItem(any(ItemInDTO.class))).thenReturn(savedDto);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemInDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Darkrai"));
    }

    @Test
    public void testCrearItemBodyInvalido() throws Exception {
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCrearItemColeccionNoExiste() throws Exception {
        ItemInDTO itemInDTO = new ItemInDTO();
        itemInDTO.setIdColeccion(999L);
        itemInDTO.setNombre("Darkrai");
        itemInDTO.setTipo("Figura");
        itemInDTO.setRareza("LEGENDARIO");

        when(itemService.crearItem(any(ItemInDTO.class)))
                .thenThrow(new ColeccionNoEncontradaException(999L));

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemInDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testModificarItemExistente() throws Exception {
        ItemPutDTO itemPutDTO = new ItemPutDTO();
        itemPutDTO.setNombre("Darkrai");
        itemPutDTO.setTipo("Figura Premium");
        itemPutDTO.setRareza("EPICO");

        ItemOutDTO response = new ItemOutDTO();
        response.setId(1L);
        response.setNombre("Darkrai");
        response.setRareza(RarezaItem.EPICO);

        when(itemService.actualizarItem(eq(1L), any(ItemPutDTO.class)))
                .thenReturn(response);

        mockMvc.perform(put("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemPutDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Darkrai"));
    }

    @Test
    public void testModificarItemNoExiste() throws Exception {
        ItemPutDTO itemPutDTO = new ItemPutDTO();
        itemPutDTO.setNombre("Darkrai");
        itemPutDTO.setTipo("Figura");
        itemPutDTO.setRareza("COMUN");

        when(itemService.actualizarItem(eq(1L), any(ItemPutDTO.class)))
                .thenThrow(new ItemNoEncontradoException(1L));

        mockMvc.perform(put("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemPutDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testModificarItemBodyInvalido() throws Exception {
        mockMvc.perform(put("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testActualizarRareza() throws Exception {
        ItemRarezaDTO rarezaDTO = new ItemRarezaDTO();
        rarezaDTO.setRareza(RarezaItem.LEGENDARIO);

        ItemOutDTO response = new ItemOutDTO();
        response.setId(1L);
        response.setRareza(RarezaItem.LEGENDARIO);

        when(itemService.actualizarRareza(eq(1L), eq(RarezaItem.LEGENDARIO))).thenReturn(response);

        mockMvc.perform(patch("/items/1/rareza")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rarezaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rareza").value("LEGENDARIO"));
    }

    @Test
    public void testActualizarRarezaItemNoExiste() throws Exception {
        ItemRarezaDTO rarezaDTO = new ItemRarezaDTO();
        rarezaDTO.setRareza(RarezaItem.LEGENDARIO);

        when(itemService.actualizarRareza(eq(999L), eq(RarezaItem.LEGENDARIO)))
                .thenThrow(new ItemNoEncontradoException(999L));

        mockMvc.perform(patch("/items/999/rareza")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rarezaDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testActualizarRarezaBodyInvalido() throws Exception {
        mockMvc.perform(patch("/items/1/rareza")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testEliminarItemExistente() throws Exception {
        mockMvc.perform(delete("/items/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testEliminarItemNoExiste() throws Exception {
        doThrow(new ItemNoEncontradoException(1L))
                .when(itemService).eliminarItem(1L);

        mockMvc.perform(delete("/items/1"))
                .andExpect(status().isNotFound());
    }
}