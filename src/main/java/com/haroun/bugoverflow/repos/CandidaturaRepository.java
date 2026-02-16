package com.haroun.bugoverflow.repos;

import com.haroun.bugoverflow.domain.Candidatura;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CandidaturaRepository extends JpaRepository<Candidatura, Long> {

    Candidatura findFirstByUsuarioId(Long id);

    Candidatura findFirstByProyectoId(Long id);

}
