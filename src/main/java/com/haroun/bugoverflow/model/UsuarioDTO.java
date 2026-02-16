package com.haroun.bugoverflow.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UsuarioDTO {

    private Long id;

    @NotNull
    @Size(max = 50)
    @UsuarioLoginUnique
    private String login;

    @NotNull
    @Size(max = 255)
    @UsuarioEmailUnique
    private String email;

    @NotNull
    @Size(max = 50)
    private String password;

    @Size(max = 50)
    private String nombre;

    @Size(max = 50)
    private String apellido;

    @NotNull
    private Boolean activo;

}
