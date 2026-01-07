package com.svalero.fancollector.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svalero.fancollector.dto.ColeccionInDTO;
import com.svalero.fancollector.dto.ColeccionOutDTO;
import com.svalero.fancollector.dto.ColeccionPutDTO;
import com.svalero.fancollector.dto.patches.ColeccionPlantillaDTO;
import com.svalero.fancollector.dto.patches.ColeccionPublicoDTO;
import com.svalero.fancollector.exception.domain.ColeccionNoEncontradaException;
import com.svalero.fancollector.service.ColeccionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ColeccionController.class)
public class ColeccionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ColeccionService coleccionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testListarColecciones() throws Exception {
        ColeccionOutDTO coleccion1 = new ColeccionOutDTO();
        coleccion1.setId(1L);
        coleccion1.setNombre("Figuras Anime");
        coleccion1.setCategoria("Anime");
        coleccion1.setIdCreador(1L);
        coleccion1.setNombreCreador("Nerea");
        coleccion1.setFechaCreacion(LocalDateTime.now());

        ColeccionOutDTO coleccion2 = new ColeccionOutDTO();
        coleccion2.setId(2L);
        coleccion2.setNombre("Trading Cards");
        coleccion2.setCategoria("Cartas");
        coleccion2.setIdCreador(1L);
        coleccion2.setNombreCreador("Nerea");
        coleccion2.setFechaCreacion(LocalDateTime.now());

        List<ColeccionOutDTO> colecciones = List.of(coleccion1, coleccion2);

        when(coleccionService.listarColecciones(null, null, null, null)).thenReturn(colecciones);

        mockMvc.perform(get("/colecciones")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Figuras Anime"))
                .andExpect(jsonPath("$[1].nombre").value("Trading Cards"));
    }

    @Test
    public void testBuscarColeccionPorIdExistente() throws Exception {
        ColeccionOutDTO coleccionOutDTO = new ColeccionOutDTO();
        coleccionOutDTO.setId(1L);
        coleccionOutDTO.setNombre("Figuras Anime");
        coleccionOutDTO.setCategoria("Anime");

        when(coleccionService.buscarColeccionPorId(1L)).thenReturn(coleccionOutDTO);

        mockMvc.perform(get("/colecciones/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Figuras Anime"));
    }

    @Test
    public void testBuscarColeccionPorIdNoExiste() throws Exception {
        when(coleccionService.buscarColeccionPorId(999L))
                .thenThrow(new ColeccionNoEncontradaException(999L));

        mockMvc.perform(get("/colecciones/999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCrearColeccionDatosValidos() throws Exception {
        ColeccionInDTO coleccionInDTO = new ColeccionInDTO();
        coleccionInDTO.setIdCreador(1L);
        coleccionInDTO.setNombre("Figuras Anime");
        coleccionInDTO.setCategoria("Anime");
        coleccionInDTO.setDescripcion("Colección de figuras");

        ColeccionOutDTO savedDto = new ColeccionOutDTO();
        savedDto.setId(1L);
        savedDto.setNombre("Figuras Anime");
        savedDto.setCategoria("Anime");

        when(coleccionService.crearColeccion(any(ColeccionInDTO.class))).thenReturn(savedDto);

        mockMvc.perform(post("/colecciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(coleccionInDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Figuras Anime"));
    }

    @Test
    public void testCrearColeccionBodyInvalido() throws Exception {
        mockMvc.perform(post("/colecciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testModificarColeccionExistente() throws Exception {
        ColeccionPutDTO coleccionPutDTO = new ColeccionPutDTO();
        coleccionPutDTO.setNombre("Figuras Anime Actualizado");
        coleccionPutDTO.setCategoria("Anime");

        ColeccionOutDTO response = new ColeccionOutDTO();
        response.setId(1L);
        response.setNombre("Figuras Anime Actualizado");

        when(coleccionService.actualizarColeccion(eq(1L), any(ColeccionPutDTO.class)))
                .thenReturn(response);

        mockMvc.perform(put("/colecciones/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(coleccionPutDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Figuras Anime Actualizado"));
    }

    @Test
    public void testModificarColeccionNoExiste() throws Exception {
        ColeccionPutDTO coleccionPutDTO = new ColeccionPutDTO();
        coleccionPutDTO.setNombre("Figuras Anime");
        coleccionPutDTO.setCategoria("Anime");

        when(coleccionService.actualizarColeccion(eq(1L), any(ColeccionPutDTO.class)))
                .thenThrow(new ColeccionNoEncontradaException(1L));

        mockMvc.perform(put("/colecciones/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(coleccionPutDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testModificarColeccionBodyInvalido() throws Exception {
        mockMvc.perform(put("/colecciones/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testActualizarEsPublica() throws Exception {
        ColeccionPublicoDTO publicoDTO = new ColeccionPublicoDTO();
        publicoDTO.setEsPublica(true);

        ColeccionOutDTO response = new ColeccionOutDTO();
        response.setId(1L);
        response.setEsPublica(true);

        when(coleccionService.actualizarEsPublica(eq(1L), eq(true))).thenReturn(response);

        mockMvc.perform(patch("/colecciones/1/publico")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(publicoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.esPublica").value(true));
    }

    @Test
    public void testActualizarUsableComoPlantilla() throws Exception {
        ColeccionPlantillaDTO plantillaDTO = new ColeccionPlantillaDTO();
        plantillaDTO.setUsableComoPlantilla(true);

        ColeccionOutDTO response = new ColeccionOutDTO();
        response.setId(1L);
        response.setUsableComoPlantilla(true);

        when(coleccionService.actualizarUsableComoPlantilla(eq(1L), eq(true))).thenReturn(response);

        mockMvc.perform(patch("/colecciones/1/plantilla")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(plantillaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usableComoPlantilla").value(true));
    }

    @Test
    public void testEliminarColeccionExistente() throws Exception {
        mockMvc.perform(delete("/colecciones/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testEliminarColeccionNoExiste() throws Exception {
        doThrow(new ColeccionNoEncontradaException(1L))
                .when(coleccionService).eliminarColeccion(1L);

        mockMvc.perform(delete("/colecciones/1"))
                .andExpect(status().isNotFound());
    }
}