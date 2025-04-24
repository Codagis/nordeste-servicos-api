package com.ordem.servico.api.controller;

import com.ordem.servico.api.model.ImageEntity;
import com.ordem.servico.api.repository.ImageRepository;
import com.ordem.servico.api.service.GCSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private GCSService gcsService;


    //REALIZA DOWNLOAD DE ARQUIVOS DO GOOGLE CLOUD STORAGE
    @GetMapping("/download/gcp/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            byte[] fileContent = gcsService.downloadFile(fileName);
            String contentType = gcsService.getContentType(fileName);
            ByteArrayResource resource = new ByteArrayResource(fileContent);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (FileNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    //REALIZA UPLOAD DE ARQUIVOS PARA O GOOGLE CLOUD STORAGE
    @PostMapping("/upload/cgp")
    public ResponseEntity<String> uploadImageGCS(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Por favor, envie um arquivo");
            }

            String imageUrl = gcsService.uploadImage(file);
            return ResponseEntity.ok("Imagem enviada com sucesso. URL: " + imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Falha ao enviar imagem");
        }
    }

    //SALVA IMAGEM NO BANCO DE DADOS
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            ImageEntity imageEntity = new ImageEntity();
            imageEntity.setFileName(file.getOriginalFilename());
            imageEntity.setImageData(file.getBytes());
            imageEntity.setUploadedAt(LocalDateTime.now());
            imageRepository.save(imageEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body("Imagem enviada com sucesso!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha no upload da imagem");
        }
    }

    //REALIZA DOWNLOAD DE UMA IMAGEM DO BANCO DE DADOS
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadImage(@PathVariable Long id) {
        Optional<ImageEntity> imageEntityOptional = imageRepository.findById(id);
        if (imageEntityOptional.isPresent()) {
            ImageEntity imageEntity = imageEntityOptional.get();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentDispositionFormData("attachment", imageEntity.getFileName());
            return new ResponseEntity<>(imageEntity.getImageData(), headers, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}