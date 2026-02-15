package com.haroun.bugoverflow.service;

import com.haroun.bugoverflow.domain.PerfilUsuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.haroun.bugoverflow.domain.PerfilUsuario}.
 */
public interface PerfilUsuarioService {
    /**
     * Save a perfilUsuario.
     *
     * @param perfilUsuario the entity to save.
     * @return the persisted entity.
     */
    PerfilUsuario save(PerfilUsuario perfilUsuario);

    /**
     * Updates a perfilUsuario.
     *
     * @param perfilUsuario the entity to update.
     * @return the persisted entity.
     */
    PerfilUsuario update(PerfilUsuario perfilUsuario);

    /**
     * Partially updates a perfilUsuario.
     *
     * @param perfilUsuario the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PerfilUsuario> partialUpdate(PerfilUsuario perfilUsuario);

    /**
     * Get all the perfilUsuarios.
     *
     * @return the list of entities.
     */
    List<PerfilUsuario> findAll();

    /**
     * Get all the perfilUsuarios with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PerfilUsuario> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" perfilUsuario.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PerfilUsuario> findOne(Long id);

    /**
     * Delete the "id" perfilUsuario.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
