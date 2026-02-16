package com.haroun.bugoverflow.service;

import com.haroun.bugoverflow.domain.Usuario;
import com.haroun.bugoverflow.events.BeforeDeleteUsuario;
import com.haroun.bugoverflow.model.UsuarioDTO;
import com.haroun.bugoverflow.repos.UsuarioRepository;
import com.haroun.bugoverflow.util.CustomCollectors;
import com.haroun.bugoverflow.util.NotFoundException;
import java.util.List;
import java.util.Map;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ApplicationEventPublisher publisher;

    public UsuarioService(final UsuarioRepository usuarioRepository,
            final ApplicationEventPublisher publisher) {
        this.usuarioRepository = usuarioRepository;
        this.publisher = publisher;
    }

    public List<UsuarioDTO> findAll() {
        final List<Usuario> usuarios = usuarioRepository.findAll(Sort.by("id"));
        return usuarios.stream()
                .map(usuario -> mapToDTO(usuario, new UsuarioDTO()))
                .toList();
    }

    public UsuarioDTO get(final Long id) {
        return usuarioRepository.findById(id)
                .map(usuario -> mapToDTO(usuario, new UsuarioDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UsuarioDTO usuarioDTO) {
        final Usuario usuario = new Usuario();
        mapToEntity(usuarioDTO, usuario);
        return usuarioRepository.save(usuario).getId();
    }

    public void update(final Long id, final UsuarioDTO usuarioDTO) {
        final Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(usuarioDTO, usuario);
        usuarioRepository.save(usuario);
    }

    public void delete(final Long id) {
        final Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteUsuario(id));
        usuarioRepository.delete(usuario);
    }

    private UsuarioDTO mapToDTO(final Usuario usuario, final UsuarioDTO usuarioDTO) {
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setLogin(usuario.getLogin());
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setPassword(usuario.getPassword());
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setApellido(usuario.getApellido());
        usuarioDTO.setActivo(usuario.getActivo());
        return usuarioDTO;
    }

    private Usuario mapToEntity(final UsuarioDTO usuarioDTO, final Usuario usuario) {
        usuario.setLogin(usuarioDTO.getLogin());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setPassword(usuarioDTO.getPassword());
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setActivo(usuarioDTO.getActivo());
        return usuario;
    }

    public boolean loginExists(final String login) {
        return usuarioRepository.existsByLoginIgnoreCase(login);
    }

    public boolean emailExists(final String email) {
        return usuarioRepository.existsByEmailIgnoreCase(email);
    }

    public Map<Long, String> getUsuarioValues() {
        return usuarioRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Usuario::getId, Usuario::getLogin));
    }

}
