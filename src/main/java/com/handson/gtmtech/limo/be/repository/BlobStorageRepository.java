package com.handson.gtmtech.limo.be.repository;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Repository;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobItem;

@Repository
public class BlobStorageRepository {
     private final BlobContainerClient containerClient;

    public BlobStorageRepository(BlobContainerClient containerClient) {
        this.containerClient = containerClient;
    }

    public void uploadFile(String blobName, InputStream data, long length) {
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        blobClient.upload(data, length, true);
    }

    public byte[] downloadFile(String blobName) {
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blobClient.downloadStream(outputStream);
        return outputStream.toByteArray();
    }

    public void deleteFile(String blobName) {
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        blobClient.delete();
    }

    public List<String> listFiles() {
        return StreamSupport.stream(containerClient.listBlobs().spliterator(), false)
                .map(BlobItem::getName)
                .collect(Collectors.toList());
    }
}
