package com.haroun.bugoverflow.web.rest;

import com.haroun.bugoverflow.domain.Proyecto;
import com.haroun.bugoverflow.repository.ProyectoRepository;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.haroun.bugoverflow.domain.Proyecto}.
 */
@RestController
@RequestMapping("/api/proyectos")
@Transactional
public class ProyectoResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProyectoResource.class);

    private static final String ENTITY_NAME = "proyecto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProyectoRepository proyectoRepository;

    public ProyectoResource(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    /**
     * {@code POST  /proyectos} : Create a new proyecto.
     *
     * @param proyecto the proyecto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new proyecto, or with status {@code 400 (Bad Request)} if the proyecto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Proyecto> createProyecto(@Valid @RequestBody Proyecto proyecto) throws URISyntaxException {
        LOG.debug("REST request to save Proyecto : {}", proyecto);
        if (proyecto.getId() != null) {
            throw new BadRequestAlertException("A new proyecto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        proyecto = proyectoRepository.save(proyecto);
        return ResponseEntity.created(new URI("/api/proyectos/" + proyecto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, proyecto.getId().toString()))
            .body(proyecto);
    }

    /**
     * {@code PUT  /proyectos/:id} : Updates an existing proyecto.
     *
     * @param id the id of the proyecto to save.
     * @param proyecto the proyecto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proyecto,
     * or with status {@code 400 (Bad Request)} if the proyecto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the proyecto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Proyecto> updateProyecto(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Proyecto proyecto
    ) throws URISyntaxException {
        LOG.debug("REST request to update Proyecto : {}, {}", id, proyecto);
        if (proyecto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, proyecto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!proyectoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        proyecto = proyectoRepository.save(proyecto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, proyecto.getId().toString()))
            .body(proyecto);
    }

    /**
     * {@code PATCH  /proyectos/:id} : Partial updates given fields of an existing proyecto, field will ignore if it is null
     *
     * @param id the id of the proyecto to save.
     * @param proyecto the proyecto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proyecto,
     * or with status {@code 400 (Bad Request)} if the proyecto is not valid,
     * or with status {@code 404 (Not Found)} if the proyecto is not found,
     * or with status {@code 500 (Internal Server Error)} if the proyecto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Proyecto> partialUpdateProyecto(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Proyecto proyecto
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Proyecto partially : {}, {}", id, proyecto);
        if (proyecto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, proyecto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!proyectoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Proyecto> result = proyectoRepository
            .findById(proyecto.getId())
            .map(existingProyecto -> {
                if (proyecto.getTitulo() != null) {
                    existingProyecto.setTitulo(proyecto.getTitulo());
                }
                if (proyecto.getDescripcion() != null) {
                    existingProyecto.setDescripcion(proyecto.getDescripcion());
                }
                if (proyecto.getUrlrepo() != null) {
                    existingProyecto.setUrlrepo(proyecto.getUrlrepo());
                }
                if (proyecto.getCategoria() != null) {
                    existingProyecto.setCategoria(proyecto.getCategoria());
                }
                if (proyecto.getEstado() != null) {
                    existingProyecto.setEstado(proyecto.getEstado());
                }

                return existingProyecto;
            })
            .map(proyectoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, proyecto.getId().toString())
        );
    }

    /**
     * {@code GET  /proyectos} : get all the proyectos.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of proyectos in body.
     */
    @GetMapping("")
    public List<Proyecto> getAllProyectos(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        LOG.debug("REST request to get all Proyectos");
        if (eagerload) {
            return proyectoRepository.findAllWithEagerRelationships();
        } else {
            return proyectoRepository.findAll();
        }
    }

    /**
     * {@code GET  /proyectos/:id} : get the "id" proyecto.
     *
     * @param id the id of the proyecto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the proyecto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Proyecto> getProyecto(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Proyecto : {}", id);
        Optional<Proyecto> proyecto = proyectoRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(proyecto);
    }

    /**
     * {@code DELETE  /proyectos/:id} : delete the "id" proyecto.
     *
     * @param id the id of the proyecto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProyecto(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Proyecto : {}", id);
        proyectoRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /proyectos/autor/:login} : get all proyectos del USUARIO logueado.
     */
    @GetMapping("/autor/{login}")
    public List<Proyecto> getProyectosByAutor(@PathVariable String login) {
        LOG.debug("REST request to get Proyectos by autor: {}", login);
        return proyectoRepository.findByAutorLogin(login);
    }

    /**
     * {@code GET  /proyectos/autor-current} : get all proyectos del USUARIO AUTENTICADO.
     */
    @GetMapping("/autor-current")
    public List<Proyecto> getProyectosByCurrentUser() {
        LOG.debug("REST request to get Proyectos by current user");
        // SecurityContextHolder.getContext().getAuthentication().getName() devuelve el login
        String currentUserLogin = SecurityContextHolder.getContext().getAuthentication().getName();
        return proyectoRepository.findByAutorLogin(currentUserLogin);
    }
}
