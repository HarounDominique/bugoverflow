package com.haroun.bugoverflow.rest;

import com.haroun.bugoverflow.model.PerfilUsuarioDTO;
import com.haroun.bugoverflow.service.PerfilUsuarioService;
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
@RequestMapping(value = "/api/perfilUsuarios", produces = MediaType.APPLICATION_JSON_VALUE)
public class PerfilUsuarioResource {

    private final PerfilUsuarioService perfilUsuarioService;
    private final UsuarioService usuarioService;
    private final SkillService skillService;

    public PerfilUsuarioResource(final PerfilUsuarioService perfilUsuarioService,
            final UsuarioService usuarioService, final SkillService skillService) {
        this.perfilUsuarioService = perfilUsuarioService;
        this.usuarioService = usuarioService;
        this.skillService = skillService;
    }

    @GetMapping
    public ResponseEntity<List<PerfilUsuarioDTO>> getAllPerfilUsuarios() {
        return ResponseEntity.ok(perfilUsuarioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfilUsuarioDTO> getPerfilUsuario(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(perfilUsuarioService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createPerfilUsuario(
            @RequestBody @Valid final PerfilUsuarioDTO perfilUsuarioDTO) {
        final Long createdId = perfilUsuarioService.create(perfilUsuarioDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updatePerfilUsuario(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final PerfilUsuarioDTO perfilUsuarioDTO) {
        perfilUsuarioService.update(id, perfilUsuarioDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerfilUsuario(@PathVariable(name = "id") final Long id) {
        perfilUsuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuarioValues")
    public ResponseEntity<Map<Long, String>> getUsuarioValues() {
        return ResponseEntity.ok(usuarioService.getUsuarioValues());
    }

    @GetMapping("/skillsValues")
    public ResponseEntity<Map<Long, String>> getSkillsValues() {
        return ResponseEntity.ok(skillService.getSkillValues());
    }

}
