package com.handson.gtmtech.limo.be.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.OffsetDateTime;

@Entity
@Table(name = "lake_status")
@Data
public class LakeStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private OffsetDateTime timestamp;
    private String sensorId; // sensor_id
    private Double temperature;
    private Double ph;
    private Double dissolvedOxygen; // dissolved_oxygen
    private Double turbidity;
    private String pollutionLevel; // pollution_level
    private OffsetDateTime recordedAt; // recorded_at

    @PrePersist
    protected void onCreate() {
        if (recordedAt == null) {
            recordedAt = OffsetDateTime.now();
        }
    }
}