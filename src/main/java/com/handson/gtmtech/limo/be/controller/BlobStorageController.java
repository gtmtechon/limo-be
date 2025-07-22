package com.handson.gtmtech.limo.be.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.handson.gtmtech.limo.be.service.BlobStorageService;

@RestController
@RequestMapping("/api/blob")
public class BlobStorageController {

    private final BlobStorageService service;

    public BlobStorageController(BlobStorageService service) {
        this.service = service;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            service.upload(file);
            return ResponseEntity.ok("Uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
        }
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<byte[]> download(@PathVariable String filename) {
        byte[] data = service.download(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(data);
    }

    @DeleteMapping("/delete/{filename}")
    public ResponseEntity<String> delete(@PathVariable String filename) {
        service.delete(filename);
        return ResponseEntity.ok("Deleted successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> list() {
        return ResponseEntity.ok(service.listAllFiles());
    }
}