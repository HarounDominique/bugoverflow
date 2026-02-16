package com.haroun.bugoverflow.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PerfilUsuarioDTO {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String nombreVisible;

    @Size(max = 500)
    private String bio;

    @Size(max = 500)
    private String github;

    @Size(max = 500)
    private String webPersonal;

    @Size(max = 500)
    private String avatarUrl;

    @NotNull
    @PerfilUsuarioUsuarioUnique
    private Long usuario;

    private List<Long> skills;

}
