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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioColeccionServiceImpl implements UsuarioColeccionService {

    @Autowired
    private UsuarioColeccionRepository usuarioColeccionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ColeccionRepository coleccionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UsuarioColeccionOutDTO crear(UsuarioColeccionInDTO dto) throws UsuarioNoEncontradoException, ColeccionNoEncontradaException {

        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new UsuarioNoEncontradoException(dto.getIdUsuario()));

        Coleccion coleccion = coleccionRepository.findById(dto.getIdColeccion())
                .orElseThrow(() -> new ColeccionNoEncontradaException(dto.getIdColeccion()));

        if (usuarioColeccionRepository.existsByUsuario_IdAndColeccion_Id(
                dto.getIdUsuario(), dto.getIdColeccion())) {
            throw new RelacionYaExisteException();
        }


        UsuarioColeccion uc = modelMapper.map(dto, UsuarioColeccion.class);
        uc.setUsuario(usuario);
        uc.setColeccion(coleccion);

        return modelMapper.map(usuarioColeccionRepository.save(uc), UsuarioColeccionOutDTO.class);
    }

    @Override
    public UsuarioColeccionOutDTO buscarPorId(Long id) throws UsuarioColeccionNoEncontradoException {
        UsuarioColeccion uc = usuarioColeccionRepository.findById(id)
                .orElseThrow(() -> new UsuarioColeccionNoEncontradoException(id));
        return modelMapper.map(uc, UsuarioColeccionOutDTO.class);
    }

    @Override
    public List<UsuarioColeccionOutDTO> listar(Long idUsuario, Long idColeccion, Boolean soloFavoritas) {

        boolean noHayFiltros = true;

        if (idUsuario != null) noHayFiltros = false;
        if (idColeccion != null) noHayFiltros = false;
        if (soloFavoritas != null) noHayFiltros = false;

        List<UsuarioColeccion> relaciones;

        if (noHayFiltros) {
            relaciones = usuarioColeccionRepository.findAll();
        } else {
            relaciones = usuarioColeccionRepository.buscarPorFiltros(idUsuario, idColeccion, soloFavoritas);
        }

        List<UsuarioColeccionOutDTO> resultado = new ArrayList<>();
        for (UsuarioColeccion uc : relaciones) {resultado.add(modelMapper.map(uc, UsuarioColeccionOutDTO.class));}
        return resultado;
    }

    @Override
    public UsuarioColeccionOutDTO actualizar(Long id, UsuarioColeccionPutDTO dto)
            throws UsuarioColeccionNoEncontradoException {

        UsuarioColeccion existente = usuarioColeccionRepository.findById(id)
                .orElseThrow(() -> new UsuarioColeccionNoEncontradoException(id));

        if (dto.getEsFavorita() != null) {
            existente.setEsFavorita(dto.getEsFavorita());}

        if (dto.getEsCreador() != null) {
            existente.setEsCreador(dto.getEsCreador());}
        return modelMapper.map(usuarioColeccionRepository.save(existente),UsuarioColeccionOutDTO.class);
    }

    @Override
    public UsuarioColeccionOutDTO actualizarFavorita(Long id, UsuarioColeccionFavoritaDTO dto)
            throws UsuarioColeccionNoEncontradoException {

        UsuarioColeccion uc = usuarioColeccionRepository.findById(id)
                .orElseThrow(() -> new UsuarioColeccionNoEncontradoException(id));

        uc.setEsFavorita(dto.getEsFavorita());

        usuarioColeccionRepository.save(uc);
        return modelMapper.map(uc, UsuarioColeccionOutDTO.class);
    }

    @Override
    public void eliminar(Long id)
            throws UsuarioColeccionNoEncontradoException {

        UsuarioColeccion uc = usuarioColeccionRepository.findById(id)
                .orElseThrow(() -> new UsuarioColeccionNoEncontradoException(id));

        usuarioColeccionRepository.delete(uc);
    }
}
