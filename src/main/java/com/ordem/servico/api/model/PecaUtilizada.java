package com.ordem.servico.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "pecas_utilizadas")
@Getter
@Setter
public class PecaUtilizada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String descricao;

    @Column(name = "quantidade_requisitada", nullable = false)
    private Integer quantidadeRequisitada;

    @Column(name = "quantidade_utilizada", nullable = false)
    private Integer quantidadeUtilizada;

    @Column(name = "quantidade_devolvida", nullable = false)
    private Integer quantidadeDevolvida;

    @ManyToOne
    @JoinColumn(name = "ordem_servico_id", nullable = false)
    private OrdemServico ordemServico;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro = LocalDateTime.now();
}