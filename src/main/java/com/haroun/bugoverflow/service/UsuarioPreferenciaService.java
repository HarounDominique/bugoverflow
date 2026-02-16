package com.haroun.bugoverflow.service;

import com.haroun.bugoverflow.domain.Usuario;
import com.haroun.bugoverflow.domain.UsuarioPreferencia;
import com.haroun.bugoverflow.events.BeforeDeleteUsuario;
import com.haroun.bugoverflow.model.UsuarioPreferenciaDTO;
import com.haroun.bugoverflow.repos.UsuarioPreferenciaRepository;
import com.haroun.bugoverflow.repos.UsuarioRepository;
import com.haroun.bugoverflow.util.NotFoundException;
import com.haroun.bugoverflow.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UsuarioPreferenciaService {

    private final UsuarioPreferenciaRepository usuarioPreferenciaRepository;
    private final UsuarioRepository usuarioRepository;

    public UsuarioPreferenciaService(
            final UsuarioPreferenciaRepository usuarioPreferenciaRepository,
            final UsuarioRepository usuarioRepository) {
        this.usuarioPreferenciaRepository = usuarioPreferenciaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<UsuarioPreferenciaDTO> findAll() {
        final List<UsuarioPreferencia> usuarioPreferencias = usuarioPreferenciaRepository.findAll(Sort.by("id"));
        return usuarioPreferencias.stream()
                .map(usuarioPreferencia -> mapToDTO(usuarioPreferencia, new UsuarioPreferenciaDTO()))
                .toList();
    }

    public UsuarioPreferenciaDTO get(final Long id) {
        return usuarioPreferenciaRepository.findById(id)
                .map(usuarioPreferencia -> mapToDTO(usuarioPreferencia, new UsuarioPreferenciaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UsuarioPreferenciaDTO usuarioPreferenciaDTO) {
        final UsuarioPreferencia usuarioPreferencia = new UsuarioPreferencia();
        mapToEntity(usuarioPreferenciaDTO, usuarioPreferencia);
        return usuarioPreferenciaRepository.save(usuarioPreferencia).getId();
    }

    public void update(final Long id, final UsuarioPreferenciaDTO usuarioPreferenciaDTO) {
        final UsuarioPreferencia usuarioPreferencia = usuarioPreferenciaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(usuarioPreferenciaDTO, usuarioPreferencia);
        usuarioPreferenciaRepository.save(usuarioPreferencia);
    }

    public void delete(final Long id) {
        final UsuarioPreferencia usuarioPreferencia = usuarioPreferenciaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        usuarioPreferenciaRepository.delete(usuarioPreferencia);
    }

    private UsuarioPreferenciaDTO mapToDTO(final UsuarioPreferencia usuarioPreferencia,
            final UsuarioPreferenciaDTO usuarioPreferenciaDTO) {
        usuarioPreferenciaDTO.setId(usuarioPreferencia.getId());
        usuarioPreferenciaDTO.setUserId(usuarioPreferencia.getUserId());
        usuarioPreferenciaDTO.setRol(usuarioPreferencia.getRol());
        usuarioPreferenciaDTO.setNivelInteres(usuarioPreferencia.getNivelInteres());
        usuarioPreferenciaDTO.setUsuario(usuarioPreferencia.getUsuario() == null ? null : usuarioPreferencia.getUsuario().getId());
        return usuarioPreferenciaDTO;
    }

    private UsuarioPreferencia mapToEntity(final UsuarioPreferenciaDTO usuarioPreferenciaDTO,
            final UsuarioPreferencia usuarioPreferencia) {
        usuarioPreferencia.setUserId(usuarioPreferenciaDTO.getUserId());
        usuarioPreferencia.setRol(usuarioPreferenciaDTO.getRol());
        usuarioPreferencia.setNivelInteres(usuarioPreferenciaDTO.getNivelInteres());
        final Usuario usuario = usuarioPreferenciaDTO.getUsuario() == null ? null : usuarioRepository.findById(usuarioPreferenciaDTO.getUsuario())
                .orElseThrow(() -> new NotFoundException("usuario not found"));
        usuarioPreferencia.setUsuario(usuario);
        return usuarioPreferencia;
    }

    @EventListener(BeforeDeleteUsuario.class)
    public void on(final BeforeDeleteUsuario event) {
        final ReferencedException referencedException = new ReferencedException();
        final UsuarioPreferencia usuarioUsuarioPreferencia = usuarioPreferenciaRepository.findFirstByUsuarioId(event.getId());
        if (usuarioUsuarioPreferencia != null) {
            referencedException.setKey("usuario.usuarioPreferencia.usuario.referenced");
            referencedException.addParam(usuarioUsuarioPreferencia.getId());
            throw referencedException;
        }
    }

}
