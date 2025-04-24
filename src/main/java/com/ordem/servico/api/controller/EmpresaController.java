package com.ordem.servico.api.controller;

import com.ordem.servico.api.model.Empresa;
import com.ordem.servico.api.service.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @GetMapping
    public ResponseEntity<List<Empresa>> listarTodas() {
        List<Empresa> empresas = empresaService.listarTodas();
        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empresa> buscarPorId(@PathVariable Long id) {
        return empresaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Empresa> salvar(@Valid @RequestBody Empresa empresa) {
        Empresa empresaSalva = empresaService.salvar(empresa);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(empresaSalva.getId())
                .toUri();
        return ResponseEntity.created(uri).body(empresaSalva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empresa> atualizar(@PathVariable Long id, @Valid @RequestBody Empresa empresa) {
        Empresa empresaAtualizada = empresaService.atualizar(id, empresa);
        return ResponseEntity.ok(empresaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        try {
            empresaService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/{empresaId}/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Empresa> salvarLogo(@PathVariable Long empresaId, @RequestParam("logo") MultipartFile logo) {
        try {
            Empresa empresaComLogo = empresaService.salvarLogo(empresaId, logo);
            return ResponseEntity.ok().body(empresaComLogo);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/{empresaId}/logo", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> buscarLogo(@PathVariable Long empresaId) {
        Optional<byte[]> logoData = empresaService.buscarLogo(empresaId);
        if (logoData.isPresent()) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(logoData.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

