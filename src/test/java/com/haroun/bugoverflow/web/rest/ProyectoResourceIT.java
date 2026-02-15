package com.haroun.bugoverflow.web.rest;

import static com.haroun.bugoverflow.domain.ProyectoAsserts.*;
import static com.haroun.bugoverflow.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haroun.bugoverflow.IntegrationTest;
import com.haroun.bugoverflow.domain.Proyecto;
import com.haroun.bugoverflow.domain.enumeration.tipocategoria;
import com.haroun.bugoverflow.domain.enumeration.tipoestado;
import com.haroun.bugoverflow.repository.ProyectoRepository;
import com.haroun.bugoverflow.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProyectoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProyectoResourceIT {

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_URLREPO = "AAAAAAAAAA";
    private static final String UPDATED_URLREPO = "BBBBBBBBBB";

    private static final tipocategoria DEFAULT_CATEGORIA = tipocategoria.WEB;
    private static final tipocategoria UPDATED_CATEGORIA = tipocategoria.MOVIL;

    private static final tipoestado DEFAULT_ESTADO = tipoestado.ABIERTO;
    private static final tipoestado UPDATED_ESTADO = tipoestado.CERRADO;

    private static final String ENTITY_API_URL = "/api/proyectos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private ProyectoRepository proyectoRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProyectoMockMvc;

    private Proyecto proyecto;

    private Proyecto insertedProyecto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proyecto createEntity() {
        return new Proyecto()
            .titulo(DEFAULT_TITULO)
            .descripcion(DEFAULT_DESCRIPCION)
            .urlrepo(DEFAULT_URLREPO)
            .categoria(DEFAULT_CATEGORIA)
            .estado(DEFAULT_ESTADO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proyecto createUpdatedEntity() {
        return new Proyecto()
            .titulo(UPDATED_TITULO)
            .descripcion(UPDATED_DESCRIPCION)
            .urlrepo(UPDATED_URLREPO)
            .categoria(UPDATED_CATEGORIA)
            .estado(UPDATED_ESTADO);
    }

    @BeforeEach
    void initTest() {
        proyecto = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProyecto != null) {
            proyectoRepository.delete(insertedProyecto);
            insertedProyecto = null;
        }
    }

    @Test
    @Transactional
    void createProyecto() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Proyecto
        var returnedProyecto = om.readValue(
            restProyectoMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proyecto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Proyecto.class
        );

        // Validate the Proyecto in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProyectoUpdatableFieldsEquals(returnedProyecto, getPersistedProyecto(returnedProyecto));

        insertedProyecto = returnedProyecto;
    }

    @Test
    @Transactional
    void createProyectoWithExistingId() throws Exception {
        // Create the Proyecto with an existing ID
        proyecto.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProyectoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proyecto)))
            .andExpect(status().isBadRequest());

        // Validate the Proyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTituloIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        proyecto.setTitulo(null);

        // Create the Proyecto, which fails.

        restProyectoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proyecto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        proyecto.setDescripcion(null);

        // Create the Proyecto, which fails.

        restProyectoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proyecto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCategoriaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        proyecto.setCategoria(null);

        // Create the Proyecto, which fails.

        restProyectoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proyecto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        proyecto.setEstado(null);

        // Create the Proyecto, which fails.

        restProyectoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proyecto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProyectos() throws Exception {
        // Initialize the database
        insertedProyecto = proyectoRepository.saveAndFlush(proyecto);

        // Get all the proyectoList
        restProyectoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proyecto.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].urlrepo").value(hasItem(DEFAULT_URLREPO)))
            .andExpect(jsonPath("$.[*].categoria").value(hasItem(DEFAULT_CATEGORIA.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProyectosWithEagerRelationshipsIsEnabled() throws Exception {
        when(proyectoRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProyectoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(proyectoRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProyectosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(proyectoRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProyectoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(proyectoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProyecto() throws Exception {
        // Initialize the database
        insertedProyecto = proyectoRepository.saveAndFlush(proyecto);

        // Get the proyecto
        restProyectoMockMvc
            .perform(get(ENTITY_API_URL_ID, proyecto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(proyecto.getId().intValue()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.urlrepo").value(DEFAULT_URLREPO))
            .andExpect(jsonPath("$.categoria").value(DEFAULT_CATEGORIA.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProyecto() throws Exception {
        // Get the proyecto
        restProyectoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProyecto() throws Exception {
        // Initialize the database
        insertedProyecto = proyectoRepository.saveAndFlush(proyecto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the proyecto
        Proyecto updatedProyecto = proyectoRepository.findById(proyecto.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProyecto are not directly saved in db
        em.detach(updatedProyecto);
        updatedProyecto
            .titulo(UPDATED_TITULO)
            .descripcion(UPDATED_DESCRIPCION)
            .urlrepo(UPDATED_URLREPO)
            .categoria(UPDATED_CATEGORIA)
            .estado(UPDATED_ESTADO);

        restProyectoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProyecto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProyecto))
            )
            .andExpect(status().isOk());

        // Validate the Proyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProyectoToMatchAllProperties(updatedProyecto);
    }

    @Test
    @Transactional
    void putNonExistingProyecto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proyecto.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProyectoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, proyecto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(proyecto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProyecto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proyecto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProyectoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(proyecto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProyecto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proyecto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProyectoMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proyecto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Proyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProyectoWithPatch() throws Exception {
        // Initialize the database
        insertedProyecto = proyectoRepository.saveAndFlush(proyecto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the proyecto using partial update
        Proyecto partialUpdatedProyecto = new Proyecto();
        partialUpdatedProyecto.setId(proyecto.getId());

        partialUpdatedProyecto.titulo(UPDATED_TITULO).urlrepo(UPDATED_URLREPO).categoria(UPDATED_CATEGORIA).estado(UPDATED_ESTADO);

        restProyectoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProyecto.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProyecto))
            )
            .andExpect(status().isOk());

        // Validate the Proyecto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProyectoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProyecto, proyecto), getPersistedProyecto(proyecto));
    }

    @Test
    @Transactional
    void fullUpdateProyectoWithPatch() throws Exception {
        // Initialize the database
        insertedProyecto = proyectoRepository.saveAndFlush(proyecto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the proyecto using partial update
        Proyecto partialUpdatedProyecto = new Proyecto();
        partialUpdatedProyecto.setId(proyecto.getId());

        partialUpdatedProyecto
            .titulo(UPDATED_TITULO)
            .descripcion(UPDATED_DESCRIPCION)
            .urlrepo(UPDATED_URLREPO)
            .categoria(UPDATED_CATEGORIA)
            .estado(UPDATED_ESTADO);

        restProyectoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProyecto.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProyecto))
            )
            .andExpect(status().isOk());

        // Validate the Proyecto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProyectoUpdatableFieldsEquals(partialUpdatedProyecto, getPersistedProyecto(partialUpdatedProyecto));
    }

    @Test
    @Transactional
    void patchNonExistingProyecto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proyecto.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProyectoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, proyecto.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(proyecto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProyecto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proyecto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProyectoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(proyecto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProyecto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proyecto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProyectoMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(proyecto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Proyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProyecto() throws Exception {
        // Initialize the database
        insertedProyecto = proyectoRepository.saveAndFlush(proyecto);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the proyecto
        restProyectoMockMvc
            .perform(delete(ENTITY_API_URL_ID, proyecto.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return proyectoRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Proyecto getPersistedProyecto(Proyecto proyecto) {
        return proyectoRepository.findById(proyecto.getId()).orElseThrow();
    }

    protected void assertPersistedProyectoToMatchAllProperties(Proyecto expectedProyecto) {
        assertProyectoAllPropertiesEquals(expectedProyecto, getPersistedProyecto(expectedProyecto));
    }

    protected void assertPersistedProyectoToMatchUpdatableProperties(Proyecto expectedProyecto) {
        assertProyectoAllUpdatablePropertiesEquals(expectedProyecto, getPersistedProyecto(expectedProyecto));
    }
}
