package com.handson.gtmtech.limo.be.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.OffsetDateTime;

@Entity
@Table(name = "devices")
@Data
public class Device {
    @Id
    private String deviceId; // device_id
    private String deviceName; // device_name
    private String deviceType; // device_type
    private String owner;
    private Double latitude;
    private Double longitude;
    private String coordSystem; // coord_system
    private OffsetDateTime createdAt; // created_at
    private String status;
    private String description;
    private OffsetDateTime lastUpdatedAt; // last_updated_at

    @PrePersist
    protected void onCreate() {
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
}