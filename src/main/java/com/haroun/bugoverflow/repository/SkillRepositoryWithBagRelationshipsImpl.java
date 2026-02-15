package com.haroun.bugoverflow.repository;

import com.haroun.bugoverflow.domain.Skill;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class SkillRepositoryWithBagRelationshipsImpl implements SkillRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String SKILLS_PARAMETER = "skills";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Skill> fetchBagRelationships(Optional<Skill> skill) {
        return skill.map(this::fetchPerfilUsuarios);
    }

    @Override
    public Page<Skill> fetchBagRelationships(Page<Skill> skills) {
        return new PageImpl<>(fetchBagRelationships(skills.getContent()), skills.getPageable(), skills.getTotalElements());
    }

    @Override
    public List<Skill> fetchBagRelationships(List<Skill> skills) {
        return Optional.of(skills).map(this::fetchPerfilUsuarios).orElse(Collections.emptyList());
    }

    Skill fetchPerfilUsuarios(Skill result) {
        return entityManager
            .createQuery("select skill from Skill skill left join fetch skill.perfilUsuarios where skill.id = :id", Skill.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Skill> fetchPerfilUsuarios(List<Skill> skills) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, skills.size()).forEach(index -> order.put(skills.get(index).getId(), index));
        List<Skill> result = entityManager
            .createQuery("select skill from Skill skill left join fetch skill.perfilUsuarios where skill in :skills", Skill.class)
            .setParameter(SKILLS_PARAMETER, skills)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
