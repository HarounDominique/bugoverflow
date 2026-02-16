package com.haroun.bugoverflow.model;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CandidaturaDTO {

    private Long id;

    @NotNull
    private LocalDateTime fecha;

    @NotNull
    private ApplicationStatus estado;

    @NotNull
    private Long usuario;

    @NotNull
    private Long proyecto;

}
