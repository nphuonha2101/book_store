package com.ecommerce.book_store.core.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfiguration {
    @Value("${firebase.credentials.url}")
    private String credentialsPath;
    @Value("${firebase.storage.bucket}")
    private String storageBucket;

    @PostConstruct
    public void initialize() {
        try {
            InputStream serviceAccount = new ClassPathResource(credentialsPath).getInputStream();

            if (!FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.getInstance().delete();
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket(storageBucket)
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            throw new RuntimeException("Error initializing Firebase", e);
        }
    }
}
