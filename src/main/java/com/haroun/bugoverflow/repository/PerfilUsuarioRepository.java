package com.haroun.bugoverflow.repository;

import com.haroun.bugoverflow.domain.PerfilUsuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PerfilUsuario entity.
 */
@Repository
public interface PerfilUsuarioRepository extends JpaRepository<PerfilUsuario, Long> {
    default Optional<PerfilUsuario> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PerfilUsuario> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PerfilUsuario> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select perfilUsuario from PerfilUsuario perfilUsuario left join fetch perfilUsuario.user",
        countQuery = "select count(perfilUsuario) from PerfilUsuario perfilUsuario"
    )
    Page<PerfilUsuario> findAllWithToOneRelationships(Pageable pageable);

    @Query("select perfilUsuario from PerfilUsuario perfilUsuario left join fetch perfilUsuario.user")
    List<PerfilUsuario> findAllWithToOneRelationships();

    @Query("select perfilUsuario from PerfilUsuario perfilUsuario left join fetch perfilUsuario.user where perfilUsuario.id =:id")
    Optional<PerfilUsuario> findOneWithToOneRelationships(@Param("id") Long id);
}
