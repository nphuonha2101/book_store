package com.ecommerce.book_store.service.implement;

import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

@Slf4j
@Service
public class FirebaseStorageService {

    @Value("${firebase.storage.bucket}")
    private String storageBucket;

    public String uploadFile(MultipartFile file, String folder) {
        try {
            String fileName = generateFileName(Objects.requireNonNull(file.getOriginalFilename()));
            String path = folder + "/" + fileName;

            Bucket bucket = StorageClient.getInstance().bucket();

            BlobId blobId = BlobId.of(storageBucket, path);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(file.getContentType())
                    .build();

            Blob blob = bucket.create(path, file.getBytes(), file.getContentType());
            blob.createAcl(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

            return String.format("https://storage.googleapis.com/%s/%s", storageBucket, path);
        } catch (IOException e) {
            log.error("Error uploading file to Firebase", e);
            throw new RuntimeException("Could not store file " + file.getOriginalFilename(), e);
        }
    }

    public void deleteFile(String fileUrl) {
        try {
            String path = new URL(fileUrl).getPath().substring(1);
            String[] parts = path.split("/", 2);
            if (parts.length < 2) {
                log.error("Invalid file URL: {}", fileUrl);
                return;
            }

            Bucket bucket = StorageClient.getInstance().bucket();
            bucket.get(parts[1]).delete();

        } catch (Exception e) {
            log.error("Error deleting file from Firebase", e);
            throw new RuntimeException("Could not delete file", e);
        }
    }

    private String generateFileName(String originalFileName) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String cleanFileName = originalFileName.replaceAll("[^a-zA-Z0-9.-]", "_");
        return timestamp + "_" + cleanFileName;
    }
}
