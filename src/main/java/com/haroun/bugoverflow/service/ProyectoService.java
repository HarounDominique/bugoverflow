package com.haroun.bugoverflow.service;

import com.haroun.bugoverflow.domain.Proyecto;
import com.haroun.bugoverflow.domain.Skill;
import com.haroun.bugoverflow.domain.Usuario;
import com.haroun.bugoverflow.events.BeforeDeleteProyecto;
import com.haroun.bugoverflow.events.BeforeDeleteSkill;
import com.haroun.bugoverflow.events.BeforeDeleteUsuario;
import com.haroun.bugoverflow.model.ProyectoDTO;
import com.haroun.bugoverflow.repos.ProyectoRepository;
import com.haroun.bugoverflow.repos.SkillRepository;
import com.haroun.bugoverflow.repos.UsuarioRepository;
import com.haroun.bugoverflow.util.CustomCollectors;
import com.haroun.bugoverflow.util.NotFoundException;
import com.haroun.bugoverflow.util.ReferencedException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(rollbackFor = Exception.class)
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SkillRepository skillRepository;
    private final ApplicationEventPublisher publisher;

    public ProyectoService(final ProyectoRepository proyectoRepository,
            final UsuarioRepository usuarioRepository, final SkillRepository skillRepository,
            final ApplicationEventPublisher publisher) {
        this.proyectoRepository = proyectoRepository;
        this.usuarioRepository = usuarioRepository;
        this.skillRepository = skillRepository;
        this.publisher = publisher;
    }

    public List<ProyectoDTO> findAll() {
        final List<Proyecto> proyectoes = proyectoRepository.findAll(Sort.by("id"));
        return proyectoes.stream()
                .map(proyecto -> mapToDTO(proyecto, new ProyectoDTO()))
                .toList();
    }

    public ProyectoDTO get(final Long id) {
        return proyectoRepository.findById(id)
                .map(proyecto -> mapToDTO(proyecto, new ProyectoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ProyectoDTO proyectoDTO) {
        final Proyecto proyecto = new Proyecto();
        mapToEntity(proyectoDTO, proyecto);
        return proyectoRepository.save(proyecto).getId();
    }

    public void update(final Long id, final ProyectoDTO proyectoDTO) {
        final Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(proyectoDTO, proyecto);
        proyectoRepository.save(proyecto);
    }

    public void delete(final Long id) {
        final Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteProyecto(id));
        proyectoRepository.delete(proyecto);
    }

    private ProyectoDTO mapToDTO(final Proyecto proyecto, final ProyectoDTO proyectoDTO) {
        proyectoDTO.setId(proyecto.getId());
        proyectoDTO.setTitulo(proyecto.getTitulo());
        proyectoDTO.setDescripcion(proyecto.getDescripcion());
        proyectoDTO.setUrlRepo(proyecto.getUrlRepo());
        proyectoDTO.setCategoria(proyecto.getCategoria());
        proyectoDTO.setEstado(proyecto.getEstado());
        proyectoDTO.setAutor(proyecto.getAutor() == null ? null : proyecto.getAutor().getId());
        proyectoDTO.setSkills(proyecto.getSkills().stream()
                .map(skill -> skill.getId())
                .toList());
        return proyectoDTO;
    }

    private Proyecto mapToEntity(final ProyectoDTO proyectoDTO, final Proyecto proyecto) {
        proyecto.setTitulo(proyectoDTO.getTitulo());
        proyecto.setDescripcion(proyectoDTO.getDescripcion());
        proyecto.setUrlRepo(proyectoDTO.getUrlRepo());
        proyecto.setCategoria(proyectoDTO.getCategoria());
        proyecto.setEstado(proyectoDTO.getEstado());
        final Usuario autor = proyectoDTO.getAutor() == null ? null : usuarioRepository.findById(proyectoDTO.getAutor())
                .orElseThrow(() -> new NotFoundException("autor not found"));
        proyecto.setAutor(autor);
        final List<Skill> skills = skillRepository.findAllById(
                proyectoDTO.getSkills() == null ? List.of() : proyectoDTO.getSkills());
        if (skills.size() != (proyectoDTO.getSkills() == null ? 0 : proyectoDTO.getSkills().size())) {
            throw new NotFoundException("one of skills not found");
        }
        proyecto.setSkills(new HashSet<>(skills));
        return proyecto;
    }

    public Map<Long, String> getProyectoValues() {
        return proyectoRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Proyecto::getId, Proyecto::getTitulo));
    }

    @EventListener(BeforeDeleteUsuario.class)
    public void on(final BeforeDeleteUsuario event) {
        final ReferencedException referencedException = new ReferencedException();
        final Proyecto autorProyecto = proyectoRepository.findFirstByAutorId(event.getId());
        if (autorProyecto != null) {
            referencedException.setKey("usuario.proyecto.autor.referenced");
            referencedException.addParam(autorProyecto.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteSkill.class)
    public void on(final BeforeDeleteSkill event) {
        // remove many-to-many relations at owning side
        proyectoRepository.findAllBySkillsId(event.getId()).forEach(proyecto ->
                proyecto.getSkills().removeIf(skill -> skill.getId().equals(event.getId())));
    }

}
