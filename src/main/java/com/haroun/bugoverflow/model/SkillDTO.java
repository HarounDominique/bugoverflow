package com.haroun.bugoverflow.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SkillDTO {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String nombre;

    @NotNull
    private SkillCategory categoria;

}
