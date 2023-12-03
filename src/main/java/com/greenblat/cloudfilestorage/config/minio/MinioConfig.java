package com.greenblat.cloudfilestorage.config.minio;

import com.greenblat.cloudfilestorage.exception.MinioOperationException;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@Configuration
@RequiredArgsConstructor
public class MinioConfig {

    private final MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioProperties.getUrl())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }


    @EventListener(ContextRefreshedEvent.class)
    public void createBucket() {
        var minioClient = minioClient();
        var bucketName = minioProperties.getBucket();
        try {
            var isFound = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build()
            );
            if (!isFound) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucketName)
                                .build()
                );
            }
        } catch (Exception e) {
            throw new MinioOperationException("Bucket '" + bucketName + "' is not created");
        }
    }
}