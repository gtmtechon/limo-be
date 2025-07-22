package com.handson.gtmtech.limo.be.repository;

import com.azure.storage.blob.*;
import com.azure.storage.blob.models.*;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
public class AzureBlobRepository {

    private final BlobContainerClient containerClient;

    public AzureBlobRepository(BlobContainerClient containerClient) {
        this.containerClient = containerClient;
    }

    // Blob 목록 조회
    public List<String> listBlobs() {
        List<String> blobNames = new ArrayList<>();
        for (BlobItem blobItem : containerClient.listBlobs()) {
            blobNames.add(blobItem.getName());
        }
        return blobNames;
    }

    // Blob 업로드
    public void uploadBlob(String blobName, InputStream data, long length) {
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        blobClient.upload(data, length, true);  // true: overwrite if exists
    }

    // Blob 다운로드
    public ByteArrayOutputStream downloadBlob(String blobName) throws IOException {
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blobClient.download(outputStream);
        return outputStream;
    }

    // Blob 삭제
    public void deleteBlob(String blobName) {
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        blobClient.delete();
    }
}