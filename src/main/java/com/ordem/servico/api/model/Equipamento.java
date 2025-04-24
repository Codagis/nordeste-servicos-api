package com.ordem.servico.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "equipamento")
@Getter
@Setter
public class Equipamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_equipamento", nullable = false, length = 50)
    private String codigoEquipamento;

    @Column(nullable = false, unique = true, length = 50)
    private String chassi;

    @Column(length = 20)
    private String horimetro;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean ativo = true;
}