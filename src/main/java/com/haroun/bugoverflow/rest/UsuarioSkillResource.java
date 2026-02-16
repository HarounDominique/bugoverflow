package com.haroun.bugoverflow.rest;

import com.haroun.bugoverflow.model.UsuarioSkillDTO;
import com.haroun.bugoverflow.service.SkillService;
import com.haroun.bugoverflow.service.UsuarioService;
import com.haroun.bugoverflow.service.UsuarioSkillService;
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
@RequestMapping(value = "/api/usuarioSkills", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsuarioSkillResource {

    private final UsuarioSkillService usuarioSkillService;
    private final UsuarioService usuarioService;
    private final SkillService skillService;

    public UsuarioSkillResource(final UsuarioSkillService usuarioSkillService,
            final UsuarioService usuarioService, final SkillService skillService) {
        this.usuarioSkillService = usuarioSkillService;
        this.usuarioService = usuarioService;
        this.skillService = skillService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioSkillDTO>> getAllUsuarioSkills() {
        return ResponseEntity.ok(usuarioSkillService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioSkillDTO> getUsuarioSkill(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(usuarioSkillService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createUsuarioSkill(
            @RequestBody @Valid final UsuarioSkillDTO usuarioSkillDTO) {
        final Long createdId = usuarioSkillService.create(usuarioSkillDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateUsuarioSkill(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final UsuarioSkillDTO usuarioSkillDTO) {
        usuarioSkillService.update(id, usuarioSkillDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuarioSkill(@PathVariable(name = "id") final Long id) {
        usuarioSkillService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuarioValues")
    public ResponseEntity<Map<Long, String>> getUsuarioValues() {
        return ResponseEntity.ok(usuarioService.getUsuarioValues());
    }

    @GetMapping("/skillValues")
    public ResponseEntity<Map<Long, String>> getSkillValues() {
        return ResponseEntity.ok(skillService.getSkillValues());
    }

}
