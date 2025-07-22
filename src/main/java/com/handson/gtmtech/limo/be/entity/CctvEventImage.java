package com.handson.gtmtech.limo.be.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.OffsetDateTime;

@Entity
@Table(name = "cctv_event_image")
@Data
public class CctvEventImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private OffsetDateTime eventTimestamp; // event_timestamp
    private String deviceId; // device_id
    private String blobName; // blob_name
    private String blobUrl; // blob_url
    private String description;
    private String eventType; // event_type
    private String processedStatus; // processed_status
    private OffsetDateTime capturedAt; // captured_at

    @PrePersist
    protected void onCreate() {
        if (capturedAt == null) {
            capturedAt = OffsetDateTime.now();
        }
    }
}