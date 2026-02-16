package com.haroun.bugoverflow.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProyectoDTO {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String titulo;

    @Size(max = 500)
    private String descripcion;

    @Size(max = 500)
    private String urlRepo;

    @Size(max = 50)
    private String categoria;

    @NotNull
    private ProjectStatus estado;

    @NotNull
    private Long autor;

    private List<Long> skills;

}
