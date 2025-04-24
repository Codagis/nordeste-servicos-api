package com.ordem.servico.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "horas_trabalhadas")
@Getter
@Setter
public class HorasTrabalhadas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false, length = 50)
    private String servico;

    @Column(nullable = false, length = 100)
    private String tecnico;

    @Column(nullable = false)
    private LocalTime inicio;

    @Column(nullable = false)
    private LocalTime termino;

    @Column(name = "horas_servico", nullable = false)
    private LocalTime horasServico;

    @ManyToOne
    @JoinColumn(name = "ordem_servico_id", nullable = false)
    private OrdemServico ordemServico;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro = LocalDateTime.now();
}