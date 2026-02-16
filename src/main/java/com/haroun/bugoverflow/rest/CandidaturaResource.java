package com.haroun.bugoverflow.rest;

import com.haroun.bugoverflow.model.CandidaturaDTO;
import com.haroun.bugoverflow.service.CandidaturaService;
import com.haroun.bugoverflow.service.ProyectoService;
import com.haroun.bugoverflow.service.UsuarioService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/candidaturas", produces = MediaType.APPLICATION_JSON_VALUE)
public class CandidaturaResource {

    private final CandidaturaService candidaturaService;
    private final UsuarioService usuarioService;
    private final ProyectoService proyectoService;

    public CandidaturaResource(final CandidaturaService candidaturaService,
            final UsuarioService usuarioService, final ProyectoService proyectoService) {
        this.candidaturaService = candidaturaService;
        this.usuarioService = usuarioService;
        this.proyectoService = proyectoService;
    }

    @GetMapping
    public ResponseEntity<List<CandidaturaDTO>> getAllCandidaturas() {
        return ResponseEntity.ok(candidaturaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidaturaDTO> getCandidatura(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(candidaturaService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createCandidatura(
            @RequestBody @Valid final CandidaturaDTO candidaturaDTO) {
        final Long createdId = candidaturaService.create(candidaturaDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateCandidatura(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final CandidaturaDTO candidaturaDTO) {
        candidaturaService.update(id, candidaturaDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidatura(@PathVariable(name = "id") final Long id) {
        candidaturaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuarioValues")
    public ResponseEntity<Map<Long, String>> getUsuarioValues() {
        return ResponseEntity.ok(usuarioService.getUsuarioValues());
    }

    @GetMapping("/proyectoValues")
    public ResponseEntity<Map<Long, String>> getProyectoValues() {
        return ResponseEntity.ok(proyectoService.getProyectoValues());
    }

}
