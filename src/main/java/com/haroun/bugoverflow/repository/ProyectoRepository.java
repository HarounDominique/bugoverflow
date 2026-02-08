package com.haroun.bugoverflow.repository;

import com.haroun.bugoverflow.domain.Proyecto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Proyecto entity.
 *
 * When extending this class, extend ProyectoRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ProyectoRepository extends ProyectoRepositoryWithBagRelationships, JpaRepository<Proyecto, Long> {
    @Query("select proyecto from Proyecto proyecto where proyecto.autor.login = ?#{authentication.name}")
    List<Proyecto> findByAutorIsCurrentUser();

    default Optional<Proyecto> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Proyecto> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Proyecto> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select proyecto from Proyecto proyecto left join fetch proyecto.autor",
        countQuery = "select count(proyecto) from Proyecto proyecto"
    )
    Page<Proyecto> findAllWithToOneRelationships(Pageable pageable);

    @Query("select proyecto from Proyecto proyecto left join fetch proyecto.autor")
    List<Proyecto> findAllWithToOneRelationships();

    @Query("select proyecto from Proyecto proyecto left join fetch proyecto.autor where proyecto.id =:id")
    Optional<Proyecto> findOneWithToOneRelationships(@Param("id") Long id);

    List<Proyecto> findByAutorLogin(String login);

    @Query(
        """
        select p
        from Proyecto p
        where p.autor.id <> :userId
        """
    )
    List<Proyecto> findAllProyectosComunidad(@Param("userId") Long userId);

    @Query(
        """
        select distinct p
        from Proyecto p
        left join fetch p.autor
        where p.autor.id <> :userId
        """
    )
    List<Proyecto> findAllProyectosComunidadWithEagerRelationships(@Param("userId") Long userId);

    @Query(
        """
        select p
        from Proyecto p
        where p.autor.id <> :userId
          and p.categoria = :categoria
        """
    )
    List<Proyecto> findAllProyectosComunidadByCategoria(@Param("userId") Long userId, @Param("categoria") String categoria);

    @Query(
        """
        select distinct p
        from Proyecto p
        left join fetch p.autor
        where p.autor.id <> :userId
          and p.categoria = :categoria
        """
    )
    List<Proyecto> findAllProyectosComunidadByCategoriaWithEagerRelationships(
        @Param("userId") Long userId,
        @Param("categoria") String categoria
    );
}
