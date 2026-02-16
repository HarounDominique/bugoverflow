package com.haroun.bugoverflow.service;

import com.haroun.bugoverflow.domain.Skill;
import com.haroun.bugoverflow.domain.Usuario;
import com.haroun.bugoverflow.domain.UsuarioSkill;
import com.haroun.bugoverflow.events.BeforeDeleteSkill;
import com.haroun.bugoverflow.events.BeforeDeleteUsuario;
import com.haroun.bugoverflow.model.UsuarioSkillDTO;
import com.haroun.bugoverflow.repos.SkillRepository;
import com.haroun.bugoverflow.repos.UsuarioRepository;
import com.haroun.bugoverflow.repos.UsuarioSkillRepository;
import com.haroun.bugoverflow.util.NotFoundException;
import com.haroun.bugoverflow.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UsuarioSkillService {

    private final UsuarioSkillRepository usuarioSkillRepository;
    private final UsuarioRepository usuarioRepository;
    private final SkillRepository skillRepository;

    public UsuarioSkillService(final UsuarioSkillRepository usuarioSkillRepository,
            final UsuarioRepository usuarioRepository, final SkillRepository skillRepository) {
        this.usuarioSkillRepository = usuarioSkillRepository;
        this.usuarioRepository = usuarioRepository;
        this.skillRepository = skillRepository;
    }

    public List<UsuarioSkillDTO> findAll() {
        final List<UsuarioSkill> usuarioSkills = usuarioSkillRepository.findAll(Sort.by("id"));
        return usuarioSkills.stream()
                .map(usuarioSkill -> mapToDTO(usuarioSkill, new UsuarioSkillDTO()))
                .toList();
    }

    public UsuarioSkillDTO get(final Long id) {
        return usuarioSkillRepository.findById(id)
                .map(usuarioSkill -> mapToDTO(usuarioSkill, new UsuarioSkillDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UsuarioSkillDTO usuarioSkillDTO) {
        final UsuarioSkill usuarioSkill = new UsuarioSkill();
        mapToEntity(usuarioSkillDTO, usuarioSkill);
        return usuarioSkillRepository.save(usuarioSkill).getId();
    }

    public void update(final Long id, final UsuarioSkillDTO usuarioSkillDTO) {
        final UsuarioSkill usuarioSkill = usuarioSkillRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(usuarioSkillDTO, usuarioSkill);
        usuarioSkillRepository.save(usuarioSkill);
    }

    public void delete(final Long id) {
        final UsuarioSkill usuarioSkill = usuarioSkillRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        usuarioSkillRepository.delete(usuarioSkill);
    }

    private UsuarioSkillDTO mapToDTO(final UsuarioSkill usuarioSkill,
            final UsuarioSkillDTO usuarioSkillDTO) {
        usuarioSkillDTO.setId(usuarioSkill.getId());
        usuarioSkillDTO.setUserId(usuarioSkill.getUserId());
        usuarioSkillDTO.setSkillId(usuarioSkill.getSkill().getId());
        usuarioSkillDTO.setNivelInteres(usuarioSkill.getNivelInteres());
        usuarioSkillDTO.setUsuario(usuarioSkill.getUsuario() == null ? null : usuarioSkill.getUsuario().getId());
        usuarioSkillDTO.setSkill(usuarioSkill.getSkill() == null ? null : usuarioSkill.getSkill().getId());
        return usuarioSkillDTO;
    }

    private UsuarioSkill mapToEntity(final UsuarioSkillDTO usuarioSkillDTO,
        final UsuarioSkill usuarioSkill) {

        // Campos simples
        usuarioSkill.setUserId(usuarioSkillDTO.getUserId());
        usuarioSkill.setNivelInteres(usuarioSkillDTO.getNivelInteres());

        // Asociar Usuario
        if (usuarioSkillDTO.getUsuario() != null) {
            Usuario usuario = usuarioRepository.findById(usuarioSkillDTO.getUsuario())
                .orElseThrow(() -> new NotFoundException("Usuario not found"));
            usuarioSkill.setUsuario(usuario);
        } else {
            usuarioSkill.setUsuario(null);
        }

        // Asociar Skill
        if (usuarioSkillDTO.getSkill() != null) {
            Skill skill = skillRepository.findById(usuarioSkillDTO.getSkill())
                .orElseThrow(() -> new NotFoundException("Skill not found"));
            usuarioSkill.setSkill(skill);
        } else {
            usuarioSkill.setSkill(null);
        }

        return usuarioSkill;
    }

    @EventListener(BeforeDeleteUsuario.class)
    public void on(final BeforeDeleteUsuario event) {
        final ReferencedException referencedException = new ReferencedException();
        final UsuarioSkill usuarioUsuarioSkill = usuarioSkillRepository.findFirstByUsuarioId(event.getId());
        if (usuarioUsuarioSkill != null) {
            referencedException.setKey("usuario.usuarioSkill.usuario.referenced");
            referencedException.addParam(usuarioUsuarioSkill.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteSkill.class)
    public void on(final BeforeDeleteSkill event) {
        final ReferencedException referencedException = new ReferencedException();
        final UsuarioSkill skillUsuarioSkill = usuarioSkillRepository.findFirstBySkillId(event.getId());
        if (skillUsuarioSkill != null) {
            referencedException.setKey("skill.usuarioSkill.skill.referenced");
            referencedException.addParam(skillUsuarioSkill.getId());
            throw referencedException;
        }
    }

}
