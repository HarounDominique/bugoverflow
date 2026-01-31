package com.haroun.bugoverflow.repository;

import com.haroun.bugoverflow.domain.Proyecto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ProyectoRepositoryWithBagRelationships {
    Optional<Proyecto> fetchBagRelationships(Optional<Proyecto> proyecto);

    List<Proyecto> fetchBagRelationships(List<Proyecto> proyectos);

    Page<Proyecto> fetchBagRelationships(Page<Proyecto> proyectos);
}
