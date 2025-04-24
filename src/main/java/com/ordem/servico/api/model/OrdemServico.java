package com.ordem.servico.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "ordem_servico")
@Getter
@Setter
public class OrdemServico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "data_abertura", nullable = false)
    private LocalDate dataAbertura;

    @Column(name = "data_fechamento")
    private LocalDate dataFechamento;

    @Column(name = "data_emissao", nullable = false)
    private LocalDateTime dataEmissao;

    @Column(name = "hora_emissao", nullable = false)
    private LocalTime horaEmissao;

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToMany
    @JoinTable(
            name = "ordem_servico_tecnico",
            joinColumns = @JoinColumn(name = "ordem_servico_id"),
            inverseJoinColumns = @JoinColumn(name = "tecnico_id")
    )
    private Set<Tecnico> tecnicos = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "equipamento_id", nullable = false)
    private Equipamento equipamento;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro = LocalDateTime.now();

    @PrePersist
    @PreUpdate
    private void validateStatus() {
        if (!List.of("Aberta", "Em andamento", "Encerrada", "Cancelada").contains(status)) {
            throw new IllegalArgumentException("Status inválido para Ordem de Serviço");
        }
    }
}