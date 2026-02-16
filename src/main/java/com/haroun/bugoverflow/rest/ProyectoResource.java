package com.haroun.bugoverflow.rest;

import com.haroun.bugoverflow.model.ProyectoDTO;
import com.haroun.bugoverflow.service.ProyectoService;
import com.haroun.bugoverflow.service.SkillService;
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
@RequestMapping(value = "/api/proyectos", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProyectoResource {

    private final ProyectoService proyectoService;
    private final UsuarioService usuarioService;
    private final SkillService skillService;

    public ProyectoResource(final ProyectoService proyectoService,
            final UsuarioService usuarioService, final SkillService skillService) {
        this.proyectoService = proyectoService;
        this.usuarioService = usuarioService;
        this.skillService = skillService;
    }

    @GetMapping
    public ResponseEntity<List<ProyectoDTO>> getAllProyectos() {
        return ResponseEntity.ok(proyectoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProyectoDTO> getProyecto(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(proyectoService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createProyecto(@RequestBody @Valid final ProyectoDTO proyectoDTO) {
        final Long createdId = proyectoService.create(proyectoDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateProyecto(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ProyectoDTO proyectoDTO) {
        proyectoService.update(id, proyectoDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProyecto(@PathVariable(name = "id") final Long id) {
        proyectoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/autorValues")
    public ResponseEntity<Map<Long, String>> getAutorValues() {
        return ResponseEntity.ok(usuarioService.getUsuarioValues());
    }

    @GetMapping("/skillsValues")
    public ResponseEntity<Map<Long, String>> getSkillsValues() {
        return ResponseEntity.ok(skillService.getSkillValues());
    }

}
