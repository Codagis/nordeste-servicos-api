package com.ordem.servico.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "servicos_realizados")
@Getter
@Setter
public class ServicosRealizados {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reclamacao_cliente", nullable = false, columnDefinition = "TEXT")
    private String reclamacaoCliente;

    @Column(name = "analise_falha", nullable = false, columnDefinition = "TEXT")
    private String analiseFalha;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String solucao;

    @OneToOne
    @JoinColumn(name = "ordem_servico_id", nullable = false, unique = true)
    private OrdemServico ordemServico;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro = LocalDateTime.now();
}