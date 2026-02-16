package com.haroun.bugoverflow.repos;

import com.haroun.bugoverflow.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SkillRepository extends JpaRepository<Skill, Long> {
}
