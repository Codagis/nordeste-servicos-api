package com.ordem.servico.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "empresa")
@Getter
@Setter
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 200)
    private String endereco;

    @Column(nullable = false, length = 50)
    private String bairro;

    @Column(nullable = false, length = 10)
    private String cep;

    @Column(nullable = false, length = 20)
    private String telefone;

    @Lob
    private byte[] logo;

    @Column(name = "logo_file_name", length = 100)
    private String logoFileName;

    @Column(name = "logo_content_type", length = 50)
    private String logoContentType;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean ativo = true;
}