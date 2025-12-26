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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ColeccionServiceImpl implements ColeccionService {

    @Autowired
    private ColeccionRepository coleccionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ColeccionOutDTO crearColeccion(ColeccionInDTO dto) throws UsuarioNoEncontradoException {

        Usuario creador = usuarioRepository.findById(dto.getIdCreador())
                .orElseThrow(() -> new UsuarioNoEncontradoException(dto.getIdCreador()));

        Coleccion coleccion = modelMapper.map(dto, Coleccion.class);
        coleccion.setCreador(creador);

        Coleccion guardada = coleccionRepository.save(coleccion);

        ColeccionOutDTO out = modelMapper.map(guardada, ColeccionOutDTO.class);

        return out;
    }

    @Override
    public ColeccionOutDTO buscarColeccionPorId(Long id) throws ColeccionNoEncontradaException {
        Coleccion coleccion = coleccionRepository.findById(id)
                .orElseThrow(() -> new ColeccionNoEncontradaException(id));

        return modelMapper.map(coleccion, ColeccionOutDTO.class);
    }

    @Override
    public List<ColeccionOutDTO> listarColecciones(String nombre, String categoria, Long idCreador, String nombreCreador) {

        boolean noHayFiltros = true;

        if (nombre != null && !nombre.isBlank()) noHayFiltros = false;
        if (categoria != null && !categoria.isBlank()) noHayFiltros = false;
        if (idCreador != null) noHayFiltros = false;
        if (nombreCreador != null && !nombreCreador.isBlank()) noHayFiltros = false;

        List<Coleccion> colecciones;

        if (noHayFiltros) {
            colecciones = coleccionRepository.findAll();
        } else {
            colecciones = coleccionRepository.buscarPorFiltros(nombre, categoria, idCreador, nombreCreador);
        }

        List<ColeccionOutDTO> resultado = new ArrayList<>();
        for (Coleccion c : colecciones) {resultado.add(modelMapper.map(c, ColeccionOutDTO.class));}
        return resultado;
    }


    @Override
    public ColeccionOutDTO actualizarColeccion(Long id, ColeccionPutDTO dto)
            throws ColeccionNoEncontradaException, UsuarioNoEncontradoException {

        Coleccion existente = coleccionRepository.findById(id)
                .orElseThrow(() -> new ColeccionNoEncontradaException(id));

        existente.setNombre(dto.getNombre());
        existente.setDescripcion(dto.getDescripcion());
        existente.setCategoria(dto.getCategoria());
        existente.setImagenPortada(dto.getImagenPortada());
        existente.setEsPublica(dto.getEsPublica());
        existente.setUsableComoPlantilla(dto.getUsableComoPlantilla());

        return modelMapper.map(coleccionRepository.save(existente),ColeccionOutDTO.class);
    }

    @Override
    public ColeccionOutDTO actualizarEsPublica(Long id, Boolean esPublica)
            throws ColeccionNoEncontradaException {

        Coleccion coleccion = coleccionRepository.findById(id)
                .orElseThrow(() -> new ColeccionNoEncontradaException(id));

        coleccion.setEsPublica(esPublica);

        return modelMapper.map(
                coleccionRepository.save(coleccion),
                ColeccionOutDTO.class
        );
    }

    @Override
    public ColeccionOutDTO actualizarUsableComoPlantilla(Long id, Boolean usableComoPlantilla)
            throws ColeccionNoEncontradaException {

        Coleccion coleccion = coleccionRepository.findById(id)
                .orElseThrow(() -> new ColeccionNoEncontradaException(id));

        coleccion.setUsableComoPlantilla(usableComoPlantilla);

        return modelMapper.map(
                coleccionRepository.save(coleccion),
                ColeccionOutDTO.class
        );
    }

    @Override
    public void eliminarColeccion(Long id) throws ColeccionNoEncontradaException {

        Coleccion coleccion = coleccionRepository.findById(id)
                .orElseThrow(() -> new ColeccionNoEncontradaException(id));

        coleccionRepository.delete(coleccion);
    }
}