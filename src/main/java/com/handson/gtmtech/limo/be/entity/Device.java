package com.handson.gtmtech.limo.be.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.security.SecureRandom;
import java.time.OffsetDateTime;

@Entity
@Table(name = "devices")
@Data
public class Device {
    @Id     @Column(name = "device_id") // DB 컬럼명과 매핑
    private String deviceId; // device_id
    @Column(name = "device_name") // DB 컬럼명과 매핑
    private String deviceName; // device_name
    @Column(name = "device_type") // DB 컬럼명과 매핑
    private String deviceType; // device_type
    private String owner;
    private Double latitude;
    private Double longitude;
    @Column(name = "coord_system") // DB 컬럼명과 매핑
    private String coordSystem; // coord_system
    @Column(name = "created_at") // DB 컬럼명과 매핑    
    private OffsetDateTime createdAt; // created_at
    private String status;
    private String description;
    @Column(name = "last_updated_at") // DB 컬럼명과 매핑
    private OffsetDateTime lastUpdatedAt; // last_updated_at

    @PrePersist
    protected void onCreate() {
        if (deviceId == null || deviceId.isEmpty()) {
            deviceId = generateRandomHash(16);
        }
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
        if (lastUpdatedAt == null) {
            lastUpdatedAt = OffsetDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdatedAt = OffsetDateTime.now();
    }

    private String generateRandomHash(int length) {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}