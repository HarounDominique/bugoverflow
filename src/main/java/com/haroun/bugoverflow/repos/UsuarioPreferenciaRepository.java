package com.haroun.bugoverflow.repos;

import com.haroun.bugoverflow.domain.UsuarioPreferencia;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioPreferenciaRepository extends JpaRepository<UsuarioPreferencia, Long> {

    UsuarioPreferencia findFirstByUsuarioId(Long id);

}
