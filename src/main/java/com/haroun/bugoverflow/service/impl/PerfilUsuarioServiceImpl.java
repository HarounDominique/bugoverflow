package com.haroun.bugoverflow.service.impl;

import com.haroun.bugoverflow.domain.PerfilUsuario;
import com.haroun.bugoverflow.repository.PerfilUsuarioRepository;
import com.haroun.bugoverflow.repository.UserRepository;
import com.haroun.bugoverflow.service.PerfilUsuarioService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.haroun.bugoverflow.domain.PerfilUsuario}.
 */
@Service
@Transactional
public class PerfilUsuarioServiceImpl implements PerfilUsuarioService {

    private static final Logger LOG = LoggerFactory.getLogger(PerfilUsuarioServiceImpl.class);

    private final PerfilUsuarioRepository perfilUsuarioRepository;

    private final UserRepository userRepository;

    public PerfilUsuarioServiceImpl(PerfilUsuarioRepository perfilUsuarioRepository, UserRepository userRepository) {
        this.perfilUsuarioRepository = perfilUsuarioRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PerfilUsuario save(PerfilUsuario perfilUsuario) {
        LOG.debug("Request to save PerfilUsuario : {}", perfilUsuario);
        Long userId = perfilUsuario.getUser().getId();
        userRepository.findById(userId).ifPresent(perfilUsuario::user);
        return perfilUsuarioRepository.save(perfilUsuario);
    }

    @Override
    public PerfilUsuario update(PerfilUsuario perfilUsuario) {
        LOG.debug("Request to update PerfilUsuario : {}", perfilUsuario);
        return perfilUsuarioRepository.save(perfilUsuario);
    }

    @Override
    public Optional<PerfilUsuario> partialUpdate(PerfilUsuario perfilUsuario) {
        LOG.debug("Request to partially update PerfilUsuario : {}", perfilUsuario);

        return perfilUsuarioRepository
            .findById(perfilUsuario.getId())
            .map(existingPerfilUsuario -> {
                if (perfilUsuario.getNombreVisible() != null) {
                    existingPerfilUsuario.setNombreVisible(perfilUsuario.getNombreVisible());
                }
                if (perfilUsuario.getBio() != null) {
                    existingPerfilUsuario.setBio(perfilUsuario.getBio());
                }
                if (perfilUsuario.getGithub() != null) {
                    existingPerfilUsuario.setGithub(perfilUsuario.getGithub());
                }
                if (perfilUsuario.getWebPersonal() != null) {
                    existingPerfilUsuario.setWebPersonal(perfilUsuario.getWebPersonal());
                }
                if (perfilUsuario.getAvatarUrl() != null) {
                    existingPerfilUsuario.setAvatarUrl(perfilUsuario.getAvatarUrl());
                }

                return existingPerfilUsuario;
            })
            .map(perfilUsuarioRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PerfilUsuario> findAll() {
        LOG.debug("Request to get all PerfilUsuarios");
        return perfilUsuarioRepository.findAll();
    }

    public Page<PerfilUsuario> findAllWithEagerRelationships(Pageable pageable) {
        return perfilUsuarioRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PerfilUsuario> findOne(Long id) {
        LOG.debug("Request to get PerfilUsuario : {}", id);
        return perfilUsuarioRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PerfilUsuario : {}", id);
        perfilUsuarioRepository.deleteById(id);
    }
}
