package com.haroun.bugoverflow.repos;

import com.haroun.bugoverflow.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByLoginIgnoreCase(String login);

    boolean existsByEmailIgnoreCase(String email);

}
