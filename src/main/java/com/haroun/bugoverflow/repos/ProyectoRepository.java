package com.haroun.bugoverflow.repos;

import com.haroun.bugoverflow.domain.Proyecto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

    Proyecto findFirstByAutorId(Long id);

    List<Proyecto> findAllBySkillsId(Long id);

}
