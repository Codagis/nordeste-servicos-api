package com.ordem.servico.api.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class GCSConfig {

    @Value("${gcp.project-id}")
    private String projectId;

    @Value("${gcp.credentials.env-variable}")
    private String credentialsEnv;

    @Bean
    public Storage storage() throws IOException {
        String credentialsJson = System.getenv(credentialsEnv);
        if (credentialsJson == null || credentialsJson.isEmpty()) {
            throw new IllegalStateException("Variável de ambiente " + credentialsEnv + " não definida ou vazia.");
        }

        try (InputStream credentialsStream = new ByteArrayInputStream(credentialsJson.getBytes(StandardCharsets.UTF_8))) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);
            return StorageOptions.newBuilder()
                    .setProjectId(projectId)
                    .setCredentials(credentials)
                    .build()
                    .getService();
        }
    }
}
