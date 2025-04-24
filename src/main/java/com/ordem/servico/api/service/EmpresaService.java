package com.ordem.servico.api.service;

import com.ordem.servico.api.model.Empresa;
import com.ordem.servico.api.repository.EmpresaRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    public List<Empresa> listarTodas() {
        return empresaRepository.findAll();
    }

    public Optional<Empresa> buscarPorId(@NotNull Long id) {
        return empresaRepository.findById(id);
    }

    @Transactional
    public Empresa salvar(@Valid Empresa empresa) {
        return empresaRepository.save(empresa);
    }

    @Transactional
    public Empresa atualizar(@NotNull Long id, @Valid Empresa empresa) {
        return empresaRepository.findById(id)
                .map(existingEmpresa -> {
                    empresa.setId(id);
                    empresa.setLogo(empresa.getLogo());
                    empresa.setLogoContentType(empresa.getLogoContentType());
                    return empresaRepository.save(empresa);
                })
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada com o ID: " + id));
    }

    @Transactional
    public void excluir(@NotNull Long id) {
        empresaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada com o ID: " + id));
        empresaRepository.deleteById(id);
    }

    @Transactional
    public Empresa salvarLogo(@NotNull Long empresaId, @NotNull MultipartFile logo) throws IOException {
        return empresaRepository.findById(empresaId)
                .map(empresa -> {
                    try {
                        empresa.setLogo(logo.getBytes());
                        empresa.setLogoFileName(logo.getOriginalFilename());
                        empresa.setLogoContentType(logo.getContentType());
                        return empresaRepository.save(empresa);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada com o ID: " + empresaId));
    }

    public Optional<byte[]> buscarLogo(@NotNull Long empresaId) {
        return empresaRepository.findById(empresaId)
                .map(Empresa::getLogo);
    }
}