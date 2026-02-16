package com.haroun.bugoverflow.service;

import com.haroun.bugoverflow.domain.Skill;
import com.haroun.bugoverflow.events.BeforeDeleteSkill;
import com.haroun.bugoverflow.model.SkillDTO;
import com.haroun.bugoverflow.repos.SkillRepository;
import com.haroun.bugoverflow.util.CustomCollectors;
import com.haroun.bugoverflow.util.NotFoundException;
import java.util.List;
import java.util.Map;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(rollbackFor = Exception.class)
public class SkillService {

    private final SkillRepository skillRepository;
    private final ApplicationEventPublisher publisher;

    public SkillService(final SkillRepository skillRepository,
            final ApplicationEventPublisher publisher) {
        this.skillRepository = skillRepository;
        this.publisher = publisher;
    }

    public List<SkillDTO> findAll() {
        final List<Skill> skills = skillRepository.findAll(Sort.by("id"));
        return skills.stream()
                .map(skill -> mapToDTO(skill, new SkillDTO()))
                .toList();
    }

    public SkillDTO get(final Long id) {
        return skillRepository.findById(id)
                .map(skill -> mapToDTO(skill, new SkillDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final SkillDTO skillDTO) {
        final Skill skill = new Skill();
        mapToEntity(skillDTO, skill);
        return skillRepository.save(skill).getId();
    }

    public void update(final Long id, final SkillDTO skillDTO) {
        final Skill skill = skillRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(skillDTO, skill);
        skillRepository.save(skill);
    }

    public void delete(final Long id) {
        final Skill skill = skillRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteSkill(id));
        skillRepository.delete(skill);
    }

    private SkillDTO mapToDTO(final Skill skill, final SkillDTO skillDTO) {
        skillDTO.setId(skill.getId());
        skillDTO.setNombre(skill.getNombre());
        skillDTO.setCategoria(skill.getCategoria());
        return skillDTO;
    }

    private Skill mapToEntity(final SkillDTO skillDTO, final Skill skill) {
        skill.setNombre(skillDTO.getNombre());
        skill.setCategoria(skillDTO.getCategoria());
        return skill;
    }

    public Map<Long, String> getSkillValues() {
        return skillRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Skill::getId, Skill::getNombre));
    }

}
