package com.ordem.servico.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tecnico")
@Getter
@Setter
public class Tecnico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String cracha;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean ativo = true;

    @ManyToMany(mappedBy = "tecnicos")
    private Set<OrdemServico> ordensServico = new HashSet<>();
}