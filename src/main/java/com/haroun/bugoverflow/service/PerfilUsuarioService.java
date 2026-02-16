package com.haroun.bugoverflow.service;

import com.haroun.bugoverflow.domain.PerfilUsuario;
import com.haroun.bugoverflow.domain.Skill;
import com.haroun.bugoverflow.domain.Usuario;
import com.haroun.bugoverflow.events.BeforeDeleteSkill;
import com.haroun.bugoverflow.events.BeforeDeleteUsuario;
import com.haroun.bugoverflow.model.PerfilUsuarioDTO;
import com.haroun.bugoverflow.repos.PerfilUsuarioRepository;
import com.haroun.bugoverflow.repos.SkillRepository;
import com.haroun.bugoverflow.repos.UsuarioRepository;
import com.haroun.bugoverflow.util.NotFoundException;
import com.haroun.bugoverflow.util.ReferencedException;
import java.util.HashSet;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(rollbackFor = Exception.class)
public class PerfilUsuarioService {

    private final PerfilUsuarioRepository perfilUsuarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final SkillRepository skillRepository;

    public PerfilUsuarioService(final PerfilUsuarioRepository perfilUsuarioRepository,
            final UsuarioRepository usuarioRepository, final SkillRepository skillRepository) {
        this.perfilUsuarioRepository = perfilUsuarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.skillRepository = skillRepository;
    }

    public List<PerfilUsuarioDTO> findAll() {
        final List<PerfilUsuario> perfilUsuarios = perfilUsuarioRepository.findAll(Sort.by("id"));
        return perfilUsuarios.stream()
                .map(perfilUsuario -> mapToDTO(perfilUsuario, new PerfilUsuarioDTO()))
                .toList();
    }

    public PerfilUsuarioDTO get(final Long id) {
        return perfilUsuarioRepository.findById(id)
                .map(perfilUsuario -> mapToDTO(perfilUsuario, new PerfilUsuarioDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PerfilUsuarioDTO perfilUsuarioDTO) {
        final PerfilUsuario perfilUsuario = new PerfilUsuario();
        mapToEntity(perfilUsuarioDTO, perfilUsuario);
        return perfilUsuarioRepository.save(perfilUsuario).getId();
    }

    public void update(final Long id, final PerfilUsuarioDTO perfilUsuarioDTO) {
        final PerfilUsuario perfilUsuario = perfilUsuarioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(perfilUsuarioDTO, perfilUsuario);
        perfilUsuarioRepository.save(perfilUsuario);
    }

    public void delete(final Long id) {
        final PerfilUsuario perfilUsuario = perfilUsuarioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        perfilUsuarioRepository.delete(perfilUsuario);
    }

    private PerfilUsuarioDTO mapToDTO(final PerfilUsuario perfilUsuario,
            final PerfilUsuarioDTO perfilUsuarioDTO) {
        perfilUsuarioDTO.setId(perfilUsuario.getId());
        perfilUsuarioDTO.setNombreVisible(perfilUsuario.getNombreVisible());
        perfilUsuarioDTO.setBio(perfilUsuario.getBio());
        perfilUsuarioDTO.setGithub(perfilUsuario.getGithub());
        perfilUsuarioDTO.setWebPersonal(perfilUsuario.getWebPersonal());
        perfilUsuarioDTO.setAvatarUrl(perfilUsuario.getAvatarUrl());
        perfilUsuarioDTO.setUsuario(perfilUsuario.getUsuario() == null ? null : perfilUsuario.getUsuario().getId());
        perfilUsuarioDTO.setSkills(perfilUsuario.getSkills().stream()
                .map(skill -> skill.getId())
                .toList());
        return perfilUsuarioDTO;
    }

    private PerfilUsuario mapToEntity(final PerfilUsuarioDTO perfilUsuarioDTO,
            final PerfilUsuario perfilUsuario) {
        perfilUsuario.setNombreVisible(perfilUsuarioDTO.getNombreVisible());
        perfilUsuario.setBio(perfilUsuarioDTO.getBio());
        perfilUsuario.setGithub(perfilUsuarioDTO.getGithub());
        perfilUsuario.setWebPersonal(perfilUsuarioDTO.getWebPersonal());
        perfilUsuario.setAvatarUrl(perfilUsuarioDTO.getAvatarUrl());
        final Usuario usuario = perfilUsuarioDTO.getUsuario() == null ? null : usuarioRepository.findById(perfilUsuarioDTO.getUsuario())
                .orElseThrow(() -> new NotFoundException("usuario not found"));
        perfilUsuario.setUsuario(usuario);
        final List<Skill> skills = skillRepository.findAllById(
                perfilUsuarioDTO.getSkills() == null ? List.of() : perfilUsuarioDTO.getSkills());
        if (skills.size() != (perfilUsuarioDTO.getSkills() == null ? 0 : perfilUsuarioDTO.getSkills().size())) {
            throw new NotFoundException("one of skills not found");
        }
        perfilUsuario.setSkills(new HashSet<>(skills));
        return perfilUsuario;
    }

    public boolean usuarioExists(final Long id) {
        return perfilUsuarioRepository.existsByUsuarioId(id);
    }

    @EventListener(BeforeDeleteUsuario.class)
    public void on(final BeforeDeleteUsuario event) {
        final ReferencedException referencedException = new ReferencedException();
        final PerfilUsuario usuarioPerfilUsuario = perfilUsuarioRepository.findFirstByUsuarioId(event.getId());
        if (usuarioPerfilUsuario != null) {
            referencedException.setKey("usuario.perfilUsuario.usuario.referenced");
            referencedException.addParam(usuarioPerfilUsuario.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteSkill.class)
    public void on(final BeforeDeleteSkill event) {
        // remove many-to-many relations at owning side
        perfilUsuarioRepository.findAllBySkillsId(event.getId()).forEach(perfilUsuario ->
                perfilUsuario.getSkills().removeIf(skill -> skill.getId().equals(event.getId())));
    }

}
