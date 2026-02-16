package com.haroun.bugoverflow.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UsuarioSkillDTO {

    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private Long skillId;

    private Integer nivelInteres;

    @NotNull
    private Long usuario;

    @NotNull
    private Long skill;

}
