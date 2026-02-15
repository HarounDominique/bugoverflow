package com.haroun.bugoverflow.domain;

import static com.haroun.bugoverflow.domain.PerfilUsuarioTestSamples.*;
import static com.haroun.bugoverflow.domain.SkillTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.haroun.bugoverflow.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PerfilUsuarioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PerfilUsuario.class);
        PerfilUsuario perfilUsuario1 = getPerfilUsuarioSample1();
        PerfilUsuario perfilUsuario2 = new PerfilUsuario();
        assertThat(perfilUsuario1).isNotEqualTo(perfilUsuario2);

        perfilUsuario2.setId(perfilUsuario1.getId());
        assertThat(perfilUsuario1).isEqualTo(perfilUsuario2);

        perfilUsuario2 = getPerfilUsuarioSample2();
        assertThat(perfilUsuario1).isNotEqualTo(perfilUsuario2);
    }

    @Test
    void skillTest() {
        PerfilUsuario perfilUsuario = getPerfilUsuarioRandomSampleGenerator();
        Skill skillBack = getSkillRandomSampleGenerator();

        perfilUsuario.addSkill(skillBack);
        assertThat(perfilUsuario.getSkills()).containsOnly(skillBack);
        assertThat(skillBack.getPerfilUsuarios()).containsOnly(perfilUsuario);

        perfilUsuario.removeSkill(skillBack);
        assertThat(perfilUsuario.getSkills()).doesNotContain(skillBack);
        assertThat(skillBack.getPerfilUsuarios()).doesNotContain(perfilUsuario);

        perfilUsuario.skills(new HashSet<>(Set.of(skillBack)));
        assertThat(perfilUsuario.getSkills()).containsOnly(skillBack);
        assertThat(skillBack.getPerfilUsuarios()).containsOnly(perfilUsuario);

        perfilUsuario.setSkills(new HashSet<>());
        assertThat(perfilUsuario.getSkills()).doesNotContain(skillBack);
        assertThat(skillBack.getPerfilUsuarios()).doesNotContain(perfilUsuario);
    }
}
