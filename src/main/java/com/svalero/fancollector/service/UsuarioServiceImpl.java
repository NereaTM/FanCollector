package com.svalero.fancollector.service;

import com.svalero.fancollector.domain.Usuario;
import com.svalero.fancollector.domain.enums.RolUsuario;
import com.svalero.fancollector.dto.UsuarioInDTO;
import com.svalero.fancollector.dto.UsuarioOutDTO;
import com.svalero.fancollector.exception.domain.UsuarioNoEncontradoException;
import com.svalero.fancollector.exception.validation.EmailDuplicadoException;
import com.svalero.fancollector.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UsuarioOutDTO crearUsuario(UsuarioInDTO usuarioInDTO) {
        if (usuarioRepository.existsByEmail(usuarioInDTO.getEmail())) {
            throw new EmailDuplicadoException(usuarioInDTO.getEmail());
        }
        Usuario usuario = modelMapper.map(usuarioInDTO, Usuario.class);
        usuario.setRol(RolUsuario.USER);

        usuarioRepository.save(usuario);

        UsuarioOutDTO resultado = modelMapper.map(usuario, UsuarioOutDTO.class);
        return resultado;
    }

    @Override
    public UsuarioOutDTO buscarUsuarioPorId(long id) throws UsuarioNoEncontradoException {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));

        UsuarioOutDTO resultado = modelMapper.map(usuario, UsuarioOutDTO.class);
        return resultado;
    }

    @Override
    public List<UsuarioOutDTO> listarUsuarios(String nombre, String email, RolUsuario rol) {
        List<Usuario> usuarios;

        boolean noHayFiltros = true;
        if (rol != null) noHayFiltros = false;
        if (nombre != null && !nombre.isBlank()) noHayFiltros = false;
        if (email != null && !email.isBlank()) noHayFiltros = false;

        if (noHayFiltros) {
            usuarios = usuarioRepository.findAll();
        } else {
            usuarios = usuarioRepository.buscarPorFiltros(nombre, email, rol);
        }

        List<UsuarioOutDTO> resultado = new ArrayList<>();
        for (Usuario u : usuarios) {
            resultado.add(modelMapper.map(u, UsuarioOutDTO.class));
        }
        return resultado;
    }

    @Override
    public UsuarioOutDTO modificarUsuario(long id, UsuarioInDTO usuarioInDTO)
            throws UsuarioNoEncontradoException {

        System.out.println("Modificando usuario ID: " + id);

        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));

        existente.setNombre(usuarioInDTO.getNombre());

        // Validar email si cambió
        String email = usuarioInDTO.getEmail();
        if (email != null && !email.equalsIgnoreCase(existente.getEmail())) {
            if (usuarioRepository.existsByEmailAndIdNot(email, id)) {
                throw new EmailDuplicadoException(email);
            }
            existente.setEmail(email);
        }

        // Actualizar contraseña solo si se la envia
        if (usuarioInDTO.getContrasena() != null && !usuarioInDTO.getContrasena().isBlank()) {
            existente.setContrasena(usuarioInDTO.getContrasena());
        }

        existente.setUrlAvatar(usuarioInDTO.getUrlAvatar());
        existente.setDescripcion(usuarioInDTO.getDescripcion());
        existente.setContactoPublico(usuarioInDTO.getContactoPublico());

        usuarioRepository.save(existente);

        return modelMapper.map(existente, UsuarioOutDTO.class);
    }

    @Override
    public UsuarioOutDTO actualizarContrasena(long id, String nuevaContrasena)
            throws UsuarioNoEncontradoException {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));

        usuario.setContrasena(nuevaContrasena);
        usuarioRepository.save(usuario);

        return modelMapper.map(usuario, UsuarioOutDTO.class);
    }

    @Override
    public void borrarUsuario(long id) throws UsuarioNoEncontradoException {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));

        usuarioRepository.delete(usuario);

        System.out.println("Usuario " + id + " eliminado");
    }
}
