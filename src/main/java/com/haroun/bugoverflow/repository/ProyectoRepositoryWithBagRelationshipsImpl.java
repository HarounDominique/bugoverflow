package com.haroun.bugoverflow.repository;

import com.haroun.bugoverflow.domain.Proyecto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ProyectoRepositoryWithBagRelationshipsImpl implements ProyectoRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String PROYECTOS_PARAMETER = "proyectos";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Proyecto> fetchBagRelationships(Optional<Proyecto> proyecto) {
        return proyecto.map(this::fetchColaboradores);
    }

    @Override
    public Page<Proyecto> fetchBagRelationships(Page<Proyecto> proyectos) {
        return new PageImpl<>(fetchBagRelationships(proyectos.getContent()), proyectos.getPageable(), proyectos.getTotalElements());
    }

    @Override
    public List<Proyecto> fetchBagRelationships(List<Proyecto> proyectos) {
        return Optional.of(proyectos).map(this::fetchColaboradores).orElse(Collections.emptyList());
    }

    Proyecto fetchColaboradores(Proyecto result) {
        return entityManager
            .createQuery(
                "select proyecto from Proyecto proyecto left join fetch proyecto.colaboradores where proyecto.id = :id",
                Proyecto.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Proyecto> fetchColaboradores(List<Proyecto> proyectos) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, proyectos.size()).forEach(index -> order.put(proyectos.get(index).getId(), index));
        List<Proyecto> result = entityManager
            .createQuery(
                "select proyecto from Proyecto proyecto left join fetch proyecto.colaboradores where proyecto in :proyectos",
                Proyecto.class
            )
            .setParameter(PROYECTOS_PARAMETER, proyectos)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
