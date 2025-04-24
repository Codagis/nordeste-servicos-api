package com.ordem.servico.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "deslocamento")
@Getter
@Setter
public class Deslocamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false, length = 10)
    private String placa;

    @Column(name = "saida_de", nullable = false, length = 100)
    private String saidaDe;

    @Column(name = "km_inicial", nullable = false, precision = 10, scale = 2)
    private BigDecimal kmInicial;

    @Column(name = "chegada_em", nullable = false, length = 100)
    private String chegadaEm;

    @Column(name = "km_final", nullable = false, precision = 10, scale = 2)
    private BigDecimal kmFinal;

    @Column(name = "total_km", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalKm;

    @ManyToOne
    @JoinColumn(name = "ordem_servico_id", nullable = false)
    private OrdemServico ordemServico;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro = LocalDateTime.now();
}