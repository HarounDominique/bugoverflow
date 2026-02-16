package com.haroun.bugoverflow.repos;

import com.haroun.bugoverflow.domain.PerfilUsuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PerfilUsuarioRepository extends JpaRepository<PerfilUsuario, Long> {

    PerfilUsuario findFirstByUsuarioId(Long id);

    List<PerfilUsuario> findAllBySkillsId(Long id);

    boolean existsByUsuarioId(Long id);

}
