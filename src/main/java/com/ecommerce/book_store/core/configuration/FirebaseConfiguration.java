package com.ecommerce.book_store.core.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.InputStream;

@Slf4j
@Configuration
public class FirebaseConfiguration {
    private final ResourceLoader resourceLoader;

    @Value("${firebase.credentials.url:}")
    private String credentialsLocation;

    @Value("${firebase.storage.bucket:}")
    private String storageBucket;

    public FirebaseConfiguration(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Firebase is optional. Storage and push notifications are disabled when no
     * service-account file is configured, so the API can still start locally
     * and in environments that do not use those features.
     */
    @PostConstruct
    public void initialize() {
        if (credentialsLocation == null || credentialsLocation.isBlank()) {
            log.warn("Firebase credentials are not configured; storage and push notifications are disabled.");
            return;
        }

        Resource resource = resourceLoader.getResource(credentialsLocation);
        try (InputStream serviceAccount = resource.getInputStream()) {
            if (!FirebaseApp.getApps().isEmpty()) {
                log.info("Firebase app already initialised.");
                return;
            }

            FirebaseOptions.Builder options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount));
            if (storageBucket != null && !storageBucket.isBlank()) {
                options.setStorageBucket(storageBucket);
            }

            FirebaseApp.initializeApp(options.build());
            log.info("Firebase initialized successfully.");
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Unable to initialise Firebase from '" + credentialsLocation + "'", e);
        }
    }

    public boolean isConfigured() {
        return credentialsLocation != null && !credentialsLocation.isBlank()
                && !FirebaseApp.getApps().isEmpty();
    }
}
