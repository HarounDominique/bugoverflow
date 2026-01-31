package com.haroun.bugoverflow.domain;

import static com.haroun.bugoverflow.domain.ProyectoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.haroun.bugoverflow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProyectoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Proyecto.class);
        Proyecto proyecto1 = getProyectoSample1();
        Proyecto proyecto2 = new Proyecto();
        assertThat(proyecto1).isNotEqualTo(proyecto2);

        proyecto2.setId(proyecto1.getId());
        assertThat(proyecto1).isEqualTo(proyecto2);

        proyecto2 = getProyectoSample2();
        assertThat(proyecto1).isNotEqualTo(proyecto2);
    }
}
