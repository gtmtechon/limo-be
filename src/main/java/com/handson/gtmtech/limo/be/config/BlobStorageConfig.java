package com.handson.gtmtech.limo.be.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

import org.springframework.beans.factory.annotation.Value;

@Configuration
public class BlobStorageConfig {

    @Value("${azure.storage.account.name}")
    private String accountName;

    @Value("${azure.storage.sas.token}")
    private String sasToken;

    @Bean
    public BlobServiceClient blobServiceClient() {
        String endpoint = String.format("https://%s.blob.core.windows.net?%s", accountName, sasToken);
        return new BlobServiceClientBuilder()
                .endpoint(endpoint)
                .buildClient();
    }

    @Bean
    public BlobContainerClient blobContainerClient(BlobServiceClient blobServiceClient,
                 @Value("${azure.storage.container.name}") String containerName) {
        return blobServiceClient.getBlobContainerClient(containerName);
    }
}