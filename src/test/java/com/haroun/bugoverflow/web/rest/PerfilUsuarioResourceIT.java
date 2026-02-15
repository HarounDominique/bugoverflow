package com.haroun.bugoverflow.web.rest;

import static com.haroun.bugoverflow.domain.PerfilUsuarioAsserts.*;
import static com.haroun.bugoverflow.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haroun.bugoverflow.IntegrationTest;
import com.haroun.bugoverflow.domain.PerfilUsuario;
import com.haroun.bugoverflow.domain.User;
import com.haroun.bugoverflow.repository.PerfilUsuarioRepository;
import com.haroun.bugoverflow.repository.UserRepository;
import com.haroun.bugoverflow.service.PerfilUsuarioService;
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
 * Integration tests for the {@link PerfilUsuarioResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PerfilUsuarioResourceIT {

    private static final String DEFAULT_NOMBRE_VISIBLE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_VISIBLE = "BBBBBBBBBB";

    private static final String DEFAULT_BIO = "AAAAAAAAAA";
    private static final String UPDATED_BIO = "BBBBBBBBBB";

    private static final String DEFAULT_GITHUB = "AAAAAAAAAA";
    private static final String UPDATED_GITHUB = "BBBBBBBBBB";

    private static final String DEFAULT_WEB_PERSONAL = "AAAAAAAAAA";
    private static final String UPDATED_WEB_PERSONAL = "BBBBBBBBBB";

    private static final String DEFAULT_AVATAR_URL = "AAAAAAAAAA";
    private static final String UPDATED_AVATAR_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/perfil-usuarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PerfilUsuarioRepository perfilUsuarioRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private PerfilUsuarioRepository perfilUsuarioRepositoryMock;

    @Mock
    private PerfilUsuarioService perfilUsuarioServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPerfilUsuarioMockMvc;

    private PerfilUsuario perfilUsuario;

    private PerfilUsuario insertedPerfilUsuario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PerfilUsuario createEntity(EntityManager em) {
        PerfilUsuario perfilUsuario = new PerfilUsuario()
            .nombreVisible(DEFAULT_NOMBRE_VISIBLE)
            .bio(DEFAULT_BIO)
            .github(DEFAULT_GITHUB)
            .webPersonal(DEFAULT_WEB_PERSONAL)
            .avatarUrl(DEFAULT_AVATAR_URL);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        perfilUsuario.setUser(user);
        return perfilUsuario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PerfilUsuario createUpdatedEntity(EntityManager em) {
        PerfilUsuario updatedPerfilUsuario = new PerfilUsuario()
            .nombreVisible(UPDATED_NOMBRE_VISIBLE)
            .bio(UPDATED_BIO)
            .github(UPDATED_GITHUB)
            .webPersonal(UPDATED_WEB_PERSONAL)
            .avatarUrl(UPDATED_AVATAR_URL);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedPerfilUsuario.setUser(user);
        return updatedPerfilUsuario;
    }

    @BeforeEach
    void initTest() {
        perfilUsuario = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPerfilUsuario != null) {
            perfilUsuarioRepository.delete(insertedPerfilUsuario);
            insertedPerfilUsuario = null;
        }
    }

    @Test
    @Transactional
    void createPerfilUsuario() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PerfilUsuario
        var returnedPerfilUsuario = om.readValue(
            restPerfilUsuarioMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(perfilUsuario))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PerfilUsuario.class
        );

        // Validate the PerfilUsuario in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPerfilUsuarioUpdatableFieldsEquals(returnedPerfilUsuario, getPersistedPerfilUsuario(returnedPerfilUsuario));

        assertPerfilUsuarioMapsIdRelationshipPersistedValue(perfilUsuario, returnedPerfilUsuario);

        insertedPerfilUsuario = returnedPerfilUsuario;
    }

    @Test
    @Transactional
    void createPerfilUsuarioWithExistingId() throws Exception {
        // Create the PerfilUsuario with an existing ID
        perfilUsuario.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPerfilUsuarioMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(perfilUsuario)))
            .andExpect(status().isBadRequest());

        // Validate the PerfilUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void updatePerfilUsuarioMapsIdAssociationWithNewId() throws Exception {
        // Initialize the database
        insertedPerfilUsuario = perfilUsuarioRepository.saveAndFlush(perfilUsuario);
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Add a new parent entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();

        // Load the perfilUsuario
        PerfilUsuario updatedPerfilUsuario = perfilUsuarioRepository.findById(perfilUsuario.getId()).orElseThrow();
        assertThat(updatedPerfilUsuario).isNotNull();
        // Disconnect from session so that the updates on updatedPerfilUsuario are not directly saved in db
        em.detach(updatedPerfilUsuario);

        // Update the User with new association value
        updatedPerfilUsuario.setUser(user);

        // Update the entity
        restPerfilUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPerfilUsuario.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPerfilUsuario))
            )
            .andExpect(status().isOk());

        // Validate the PerfilUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        /**
         * Validate the id for MapsId, the ids must be same
         * Uncomment the following line for assertion. However, please note that there is a known issue and uncommenting will fail the test.
         * Please look at https://github.com/jhipster/generator-jhipster/issues/9100. You can modify this test as necessary.
         * assertThat(testPerfilUsuario.getId()).isEqualTo(testPerfilUsuario.getUser().getId());
         */
    }

    @Test
    @Transactional
    void checkNombreVisibleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        perfilUsuario.setNombreVisible(null);

        // Create the PerfilUsuario, which fails.

        restPerfilUsuarioMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(perfilUsuario)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPerfilUsuarios() throws Exception {
        // Initialize the database
        insertedPerfilUsuario = perfilUsuarioRepository.saveAndFlush(perfilUsuario);

        // Get all the perfilUsuarioList
        restPerfilUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(perfilUsuario.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreVisible").value(hasItem(DEFAULT_NOMBRE_VISIBLE)))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO)))
            .andExpect(jsonPath("$.[*].github").value(hasItem(DEFAULT_GITHUB)))
            .andExpect(jsonPath("$.[*].webPersonal").value(hasItem(DEFAULT_WEB_PERSONAL)))
            .andExpect(jsonPath("$.[*].avatarUrl").value(hasItem(DEFAULT_AVATAR_URL)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPerfilUsuariosWithEagerRelationshipsIsEnabled() throws Exception {
        when(perfilUsuarioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPerfilUsuarioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(perfilUsuarioServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPerfilUsuariosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(perfilUsuarioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPerfilUsuarioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(perfilUsuarioRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPerfilUsuario() throws Exception {
        // Initialize the database
        insertedPerfilUsuario = perfilUsuarioRepository.saveAndFlush(perfilUsuario);

        // Get the perfilUsuario
        restPerfilUsuarioMockMvc
            .perform(get(ENTITY_API_URL_ID, perfilUsuario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(perfilUsuario.getId().intValue()))
            .andExpect(jsonPath("$.nombreVisible").value(DEFAULT_NOMBRE_VISIBLE))
            .andExpect(jsonPath("$.bio").value(DEFAULT_BIO))
            .andExpect(jsonPath("$.github").value(DEFAULT_GITHUB))
            .andExpect(jsonPath("$.webPersonal").value(DEFAULT_WEB_PERSONAL))
            .andExpect(jsonPath("$.avatarUrl").value(DEFAULT_AVATAR_URL));
    }

    @Test
    @Transactional
    void getNonExistingPerfilUsuario() throws Exception {
        // Get the perfilUsuario
        restPerfilUsuarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPerfilUsuario() throws Exception {
        // Initialize the database
        insertedPerfilUsuario = perfilUsuarioRepository.saveAndFlush(perfilUsuario);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the perfilUsuario
        PerfilUsuario updatedPerfilUsuario = perfilUsuarioRepository.findById(perfilUsuario.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPerfilUsuario are not directly saved in db
        em.detach(updatedPerfilUsuario);
        updatedPerfilUsuario
            .nombreVisible(UPDATED_NOMBRE_VISIBLE)
            .bio(UPDATED_BIO)
            .github(UPDATED_GITHUB)
            .webPersonal(UPDATED_WEB_PERSONAL)
            .avatarUrl(UPDATED_AVATAR_URL);

        restPerfilUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPerfilUsuario.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPerfilUsuario))
            )
            .andExpect(status().isOk());

        // Validate the PerfilUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPerfilUsuarioToMatchAllProperties(updatedPerfilUsuario);
    }

    @Test
    @Transactional
    void putNonExistingPerfilUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        perfilUsuario.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPerfilUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, perfilUsuario.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(perfilUsuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the PerfilUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPerfilUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        perfilUsuario.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerfilUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(perfilUsuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the PerfilUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPerfilUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        perfilUsuario.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerfilUsuarioMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(perfilUsuario)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PerfilUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePerfilUsuarioWithPatch() throws Exception {
        // Initialize the database
        insertedPerfilUsuario = perfilUsuarioRepository.saveAndFlush(perfilUsuario);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the perfilUsuario using partial update
        PerfilUsuario partialUpdatedPerfilUsuario = new PerfilUsuario();
        partialUpdatedPerfilUsuario.setId(perfilUsuario.getId());

        partialUpdatedPerfilUsuario.github(UPDATED_GITHUB).avatarUrl(UPDATED_AVATAR_URL);

        restPerfilUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPerfilUsuario.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPerfilUsuario))
            )
            .andExpect(status().isOk());

        // Validate the PerfilUsuario in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPerfilUsuarioUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPerfilUsuario, perfilUsuario),
            getPersistedPerfilUsuario(perfilUsuario)
        );
    }

    @Test
    @Transactional
    void fullUpdatePerfilUsuarioWithPatch() throws Exception {
        // Initialize the database
        insertedPerfilUsuario = perfilUsuarioRepository.saveAndFlush(perfilUsuario);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the perfilUsuario using partial update
        PerfilUsuario partialUpdatedPerfilUsuario = new PerfilUsuario();
        partialUpdatedPerfilUsuario.setId(perfilUsuario.getId());

        partialUpdatedPerfilUsuario
            .nombreVisible(UPDATED_NOMBRE_VISIBLE)
            .bio(UPDATED_BIO)
            .github(UPDATED_GITHUB)
            .webPersonal(UPDATED_WEB_PERSONAL)
            .avatarUrl(UPDATED_AVATAR_URL);

        restPerfilUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPerfilUsuario.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPerfilUsuario))
            )
            .andExpect(status().isOk());

        // Validate the PerfilUsuario in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPerfilUsuarioUpdatableFieldsEquals(partialUpdatedPerfilUsuario, getPersistedPerfilUsuario(partialUpdatedPerfilUsuario));
    }

    @Test
    @Transactional
    void patchNonExistingPerfilUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        perfilUsuario.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPerfilUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, perfilUsuario.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(perfilUsuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the PerfilUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPerfilUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        perfilUsuario.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerfilUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(perfilUsuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the PerfilUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPerfilUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        perfilUsuario.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerfilUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(perfilUsuario))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PerfilUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePerfilUsuario() throws Exception {
        // Initialize the database
        insertedPerfilUsuario = perfilUsuarioRepository.saveAndFlush(perfilUsuario);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the perfilUsuario
        restPerfilUsuarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, perfilUsuario.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return perfilUsuarioRepository.count();
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

    protected PerfilUsuario getPersistedPerfilUsuario(PerfilUsuario perfilUsuario) {
        return perfilUsuarioRepository.findById(perfilUsuario.getId()).orElseThrow();
    }

    protected void assertPersistedPerfilUsuarioToMatchAllProperties(PerfilUsuario expectedPerfilUsuario) {
        assertPerfilUsuarioAllPropertiesEquals(expectedPerfilUsuario, getPersistedPerfilUsuario(expectedPerfilUsuario));
    }

    protected void assertPersistedPerfilUsuarioToMatchUpdatableProperties(PerfilUsuario expectedPerfilUsuario) {
        assertPerfilUsuarioAllUpdatablePropertiesEquals(expectedPerfilUsuario, getPersistedPerfilUsuario(expectedPerfilUsuario));
    }
}
