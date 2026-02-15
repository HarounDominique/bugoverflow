package com.haroun.bugoverflow.web.rest;

import com.haroun.bugoverflow.domain.PerfilUsuario;
import com.haroun.bugoverflow.repository.PerfilUsuarioRepository;
import com.haroun.bugoverflow.service.PerfilUsuarioService;
import com.haroun.bugoverflow.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.haroun.bugoverflow.domain.PerfilUsuario}.
 */
@RestController
@RequestMapping("/api/perfil-usuarios")
public class PerfilUsuarioResource {

    private static final Logger LOG = LoggerFactory.getLogger(PerfilUsuarioResource.class);

    private static final String ENTITY_NAME = "perfilUsuario";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PerfilUsuarioService perfilUsuarioService;

    private final PerfilUsuarioRepository perfilUsuarioRepository;

    public PerfilUsuarioResource(PerfilUsuarioService perfilUsuarioService, PerfilUsuarioRepository perfilUsuarioRepository) {
        this.perfilUsuarioService = perfilUsuarioService;
        this.perfilUsuarioRepository = perfilUsuarioRepository;
    }

    /**
     * {@code POST  /perfil-usuarios} : Create a new perfilUsuario.
     *
     * @param perfilUsuario the perfilUsuario to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new perfilUsuario, or with status {@code 400 (Bad Request)} if the perfilUsuario has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PerfilUsuario> createPerfilUsuario(@Valid @RequestBody PerfilUsuario perfilUsuario) throws URISyntaxException {
        LOG.debug("REST request to save PerfilUsuario : {}", perfilUsuario);
        if (perfilUsuario.getId() != null) {
            throw new BadRequestAlertException("A new perfilUsuario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (Objects.isNull(perfilUsuario.getUser())) {
            throw new BadRequestAlertException("Invalid association value provided", ENTITY_NAME, "null");
        }
        perfilUsuario = perfilUsuarioService.save(perfilUsuario);
        return ResponseEntity.created(new URI("/api/perfil-usuarios/" + perfilUsuario.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, perfilUsuario.getId().toString()))
            .body(perfilUsuario);
    }

    /**
     * {@code PUT  /perfil-usuarios/:id} : Updates an existing perfilUsuario.
     *
     * @param id the id of the perfilUsuario to save.
     * @param perfilUsuario the perfilUsuario to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated perfilUsuario,
     * or with status {@code 400 (Bad Request)} if the perfilUsuario is not valid,
     * or with status {@code 500 (Internal Server Error)} if the perfilUsuario couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PerfilUsuario> updatePerfilUsuario(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PerfilUsuario perfilUsuario
    ) throws URISyntaxException {
        LOG.debug("REST request to update PerfilUsuario : {}, {}", id, perfilUsuario);
        if (perfilUsuario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, perfilUsuario.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!perfilUsuarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        perfilUsuario = perfilUsuarioService.update(perfilUsuario);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, perfilUsuario.getId().toString()))
            .body(perfilUsuario);
    }

    /**
     * {@code PATCH  /perfil-usuarios/:id} : Partial updates given fields of an existing perfilUsuario, field will ignore if it is null
     *
     * @param id the id of the perfilUsuario to save.
     * @param perfilUsuario the perfilUsuario to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated perfilUsuario,
     * or with status {@code 400 (Bad Request)} if the perfilUsuario is not valid,
     * or with status {@code 404 (Not Found)} if the perfilUsuario is not found,
     * or with status {@code 500 (Internal Server Error)} if the perfilUsuario couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PerfilUsuario> partialUpdatePerfilUsuario(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PerfilUsuario perfilUsuario
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PerfilUsuario partially : {}, {}", id, perfilUsuario);
        if (perfilUsuario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, perfilUsuario.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!perfilUsuarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PerfilUsuario> result = perfilUsuarioService.partialUpdate(perfilUsuario);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, perfilUsuario.getId().toString())
        );
    }

    /**
     * {@code GET  /perfil-usuarios} : get all the perfilUsuarios.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of perfilUsuarios in body.
     */
    @GetMapping("")
    public List<PerfilUsuario> getAllPerfilUsuarios(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all PerfilUsuarios");
        return perfilUsuarioService.findAll();
    }

    /**
     * {@code GET  /perfil-usuarios/:id} : get the "id" perfilUsuario.
     *
     * @param id the id of the perfilUsuario to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the perfilUsuario, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PerfilUsuario> getPerfilUsuario(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PerfilUsuario : {}", id);
        Optional<PerfilUsuario> perfilUsuario = perfilUsuarioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(perfilUsuario);
    }

    /**
     * {@code DELETE  /perfil-usuarios/:id} : delete the "id" perfilUsuario.
     *
     * @param id the id of the perfilUsuario to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerfilUsuario(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PerfilUsuario : {}", id);
        perfilUsuarioService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
