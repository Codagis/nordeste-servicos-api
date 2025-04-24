package com.ordem.servico.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "imagens")
@Getter
@Setter
public class Imagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "image_data", nullable = false)
    private byte[] imageData;

    @Column(name = "file_name", nullable = false, length = 100)
    private String fileName;

    @Column(name = "data_upload", nullable = false)
    private LocalDateTime dataUpload = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "ordem_servico_id", nullable = false)
    private OrdemServico ordemServico;
}