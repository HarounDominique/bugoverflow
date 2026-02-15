package com.haroun.bugoverflow.repository;

import com.haroun.bugoverflow.domain.Skill;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface SkillRepositoryWithBagRelationships {
    Optional<Skill> fetchBagRelationships(Optional<Skill> skill);

    List<Skill> fetchBagRelationships(List<Skill> skills);

    Page<Skill> fetchBagRelationships(Page<Skill> skills);
}
