package com.haroun.bugoverflow.rest;

import com.haroun.bugoverflow.model.UsuarioPreferenciaDTO;
import com.haroun.bugoverflow.service.UsuarioPreferenciaService;
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
@RequestMapping(value = "/api/usuarioPreferencias", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsuarioPreferenciaResource {

    private final UsuarioPreferenciaService usuarioPreferenciaService;
    private final UsuarioService usuarioService;

    public UsuarioPreferenciaResource(final UsuarioPreferenciaService usuarioPreferenciaService,
            final UsuarioService usuarioService) {
        this.usuarioPreferenciaService = usuarioPreferenciaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioPreferenciaDTO>> getAllUsuarioPreferencias() {
        return ResponseEntity.ok(usuarioPreferenciaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioPreferenciaDTO> getUsuarioPreferencia(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(usuarioPreferenciaService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createUsuarioPreferencia(
            @RequestBody @Valid final UsuarioPreferenciaDTO usuarioPreferenciaDTO) {
        final Long createdId = usuarioPreferenciaService.create(usuarioPreferenciaDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateUsuarioPreferencia(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final UsuarioPreferenciaDTO usuarioPreferenciaDTO) {
        usuarioPreferenciaService.update(id, usuarioPreferenciaDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuarioPreferencia(@PathVariable(name = "id") final Long id) {
        usuarioPreferenciaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuarioValues")
    public ResponseEntity<Map<Long, String>> getUsuarioValues() {
        return ResponseEntity.ok(usuarioService.getUsuarioValues());
    }

}
