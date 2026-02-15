package com.haroun.bugoverflow.domain;

import static com.haroun.bugoverflow.domain.PerfilUsuarioTestSamples.*;
import static com.haroun.bugoverflow.domain.SkillTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.haroun.bugoverflow.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SkillTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Skill.class);
        Skill skill1 = getSkillSample1();
        Skill skill2 = new Skill();
        assertThat(skill1).isNotEqualTo(skill2);

        skill2.setId(skill1.getId());
        assertThat(skill1).isEqualTo(skill2);

        skill2 = getSkillSample2();
        assertThat(skill1).isNotEqualTo(skill2);
    }

    @Test
    void perfilUsuarioTest() {
        Skill skill = getSkillRandomSampleGenerator();
        PerfilUsuario perfilUsuarioBack = getPerfilUsuarioRandomSampleGenerator();

        skill.addPerfilUsuario(perfilUsuarioBack);
        assertThat(skill.getPerfilUsuarios()).containsOnly(perfilUsuarioBack);

        skill.removePerfilUsuario(perfilUsuarioBack);
        assertThat(skill.getPerfilUsuarios()).doesNotContain(perfilUsuarioBack);

        skill.perfilUsuarios(new HashSet<>(Set.of(perfilUsuarioBack)));
        assertThat(skill.getPerfilUsuarios()).containsOnly(perfilUsuarioBack);

        skill.setPerfilUsuarios(new HashSet<>());
        assertThat(skill.getPerfilUsuarios()).doesNotContain(perfilUsuarioBack);
    }
}
