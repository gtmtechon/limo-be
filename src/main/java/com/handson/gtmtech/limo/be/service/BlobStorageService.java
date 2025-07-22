package com.handson.gtmtech.limo.be.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.handson.gtmtech.limo.be.repository.BlobStorageRepository;

@Service
public class BlobStorageService {

    private final BlobStorageRepository repository;

    public BlobStorageService(BlobStorageRepository repository) {
        this.repository = repository;
    }

    public void upload(MultipartFile file) throws IOException {
        repository.uploadFile(file.getOriginalFilename(), file.getInputStream(), file.getSize());
    }

    public byte[] download(String filename) {
        return repository.downloadFile(filename);
    }

    public void delete(String filename) {
        repository.deleteFile(filename);
    }

    public List<String> listAllFiles() {
        return repository.listFiles();
    }
}