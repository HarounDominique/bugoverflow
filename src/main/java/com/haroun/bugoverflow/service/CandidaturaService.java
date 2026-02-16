package com.haroun.bugoverflow.service;

import com.haroun.bugoverflow.domain.Candidatura;
import com.haroun.bugoverflow.domain.Proyecto;
import com.haroun.bugoverflow.domain.Usuario;
import com.haroun.bugoverflow.events.BeforeDeleteProyecto;
import com.haroun.bugoverflow.events.BeforeDeleteUsuario;
import com.haroun.bugoverflow.model.CandidaturaDTO;
import com.haroun.bugoverflow.repos.CandidaturaRepository;
import com.haroun.bugoverflow.repos.ProyectoRepository;
import com.haroun.bugoverflow.repos.UsuarioRepository;
import com.haroun.bugoverflow.util.NotFoundException;
import com.haroun.bugoverflow.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CandidaturaService {

    private final CandidaturaRepository candidaturaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProyectoRepository proyectoRepository;

    public CandidaturaService(final CandidaturaRepository candidaturaRepository,
            final UsuarioRepository usuarioRepository,
            final ProyectoRepository proyectoRepository) {
        this.candidaturaRepository = candidaturaRepository;
        this.usuarioRepository = usuarioRepository;
        this.proyectoRepository = proyectoRepository;
    }

    public List<CandidaturaDTO> findAll() {
        final List<Candidatura> candidaturas = candidaturaRepository.findAll(Sort.by("id"));
        return candidaturas.stream()
                .map(candidatura -> mapToDTO(candidatura, new CandidaturaDTO()))
                .toList();
    }

    public CandidaturaDTO get(final Long id) {
        return candidaturaRepository.findById(id)
                .map(candidatura -> mapToDTO(candidatura, new CandidaturaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CandidaturaDTO candidaturaDTO) {
        final Candidatura candidatura = new Candidatura();
        mapToEntity(candidaturaDTO, candidatura);
        return candidaturaRepository.save(candidatura).getId();
    }

    public void update(final Long id, final CandidaturaDTO candidaturaDTO) {
        final Candidatura candidatura = candidaturaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(candidaturaDTO, candidatura);
        candidaturaRepository.save(candidatura);
    }

    public void delete(final Long id) {
        final Candidatura candidatura = candidaturaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        candidaturaRepository.delete(candidatura);
    }

    private CandidaturaDTO mapToDTO(final Candidatura candidatura,
            final CandidaturaDTO candidaturaDTO) {
        candidaturaDTO.setId(candidatura.getId());
        candidaturaDTO.setFecha(candidatura.getFecha());
        candidaturaDTO.setEstado(candidatura.getEstado());
        candidaturaDTO.setUsuario(candidatura.getUsuario() == null ? null : candidatura.getUsuario().getId());
        candidaturaDTO.setProyecto(candidatura.getProyecto() == null ? null : candidatura.getProyecto().getId());
        return candidaturaDTO;
    }

    private Candidatura mapToEntity(final CandidaturaDTO candidaturaDTO,
            final Candidatura candidatura) {
        candidatura.setFecha(candidaturaDTO.getFecha());
        candidatura.setEstado(candidaturaDTO.getEstado());
        final Usuario usuario = candidaturaDTO.getUsuario() == null ? null : usuarioRepository.findById(candidaturaDTO.getUsuario())
                .orElseThrow(() -> new NotFoundException("usuario not found"));
        candidatura.setUsuario(usuario);
        final Proyecto proyecto = candidaturaDTO.getProyecto() == null ? null : proyectoRepository.findById(candidaturaDTO.getProyecto())
                .orElseThrow(() -> new NotFoundException("proyecto not found"));
        candidatura.setProyecto(proyecto);
        return candidatura;
    }

    @EventListener(BeforeDeleteUsuario.class)
    public void on(final BeforeDeleteUsuario event) {
        final ReferencedException referencedException = new ReferencedException();
        final Candidatura usuarioCandidatura = candidaturaRepository.findFirstByUsuarioId(event.getId());
        if (usuarioCandidatura != null) {
            referencedException.setKey("usuario.candidatura.usuario.referenced");
            referencedException.addParam(usuarioCandidatura.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteProyecto.class)
    public void on(final BeforeDeleteProyecto event) {
        final ReferencedException referencedException = new ReferencedException();
        final Candidatura proyectoCandidatura = candidaturaRepository.findFirstByProyectoId(event.getId());
        if (proyectoCandidatura != null) {
            referencedException.setKey("proyecto.candidatura.proyecto.referenced");
            referencedException.addParam(proyectoCandidatura.getId());
            throw referencedException;
        }
    }

}
