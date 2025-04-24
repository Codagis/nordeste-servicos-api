package com.ordem.servico.api.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Service
public class GCSService {

    @Autowired
    private Storage storage;

    @Value("${gcp.bucket-name}")
    private String bucketName;

    public String uploadImage(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        storage.create(blobInfo, file.getBytes());
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
    }

    public byte[] downloadFile(String fileName) throws IOException {
        Blob blob = storage.get(bucketName, fileName);
        if (blob == null) {
            throw new FileNotFoundException("Arquivo não encontrado: " + fileName);
        }
        return blob.getContent();
    }

    public String getContentType(String fileName) {
        Blob blob = storage.get(bucketName, fileName);
        return blob != null ? blob.getContentType() : "application/octet-stream";
    }
}