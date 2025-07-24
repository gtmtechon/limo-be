package com.handson.gtmtech.limo.be.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "lake_status")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LakeStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private OffsetDateTime ttimestamp;
    @Column(name = "sensor_id") // DB 컬럼명과 매핑
    private String sensorId; // sensor_id
    private Double temperature;
    private Double ph;
    @Column(name = "dissolved_oxygen") // DB 컬럼명과 매핑
    private Double dissolvedOxygen; // dissolved_oxygen
    private Double turbidity;
    @Column(name = "pollution_level") // DB 컬럼명과 매핑
    private String pollutionLevel; // pollution_level
    @Column(name = "recorded_at") // DB 컬럼명과 매핑
    private OffsetDateTime recordedAt; // recorded_at

    @PrePersist
    protected void onCreate() {
        if (recordedAt == null) {
            recordedAt = OffsetDateTime.now();
        }
    }
}