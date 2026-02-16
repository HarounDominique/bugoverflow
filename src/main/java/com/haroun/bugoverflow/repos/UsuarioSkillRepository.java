package com.haroun.bugoverflow.repos;

import com.haroun.bugoverflow.domain.UsuarioSkill;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioSkillRepository extends JpaRepository<UsuarioSkill, Long> {

    UsuarioSkill findFirstByUsuarioId(Long id);

    UsuarioSkill findFirstBySkillId(Long id);

}
