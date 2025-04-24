package com.ordem.servico.api.controller;

import com.ordem.servico.api.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
@RequestMapping("/relatorio")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping(value = "/generate", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generatePdf() throws IOException {
        try {
            byte[] pdfBytes = reportService.criarRelatorio();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=relatorio_assistencia_tecnica.pdf");
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (Exception e) {
            throw new IOException("Erro ao gerar PDF: " + e.getMessage(), e);
        }
    }
}