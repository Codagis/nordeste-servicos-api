package com.ordem.servico.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "cliente")
@Getter
@Setter
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 200)
    private String endereco;

    @Column(nullable = false, length = 10)
    private String cep;

    @Column(nullable = false, length = 50)
    private String bairro;

    @Column(nullable = false, length = 50)
    private String cidade;

    @Column(nullable = false, length = 20)
    private String telefone;

    @Column(length = 100)
    private String email;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean ativo = true;
}