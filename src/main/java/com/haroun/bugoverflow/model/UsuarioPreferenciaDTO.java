package com.haroun.bugoverflow.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UsuarioPreferenciaDTO {

    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private RoleType rol;

    private Integer nivelInteres;

    @NotNull
    private Long usuario;

}
