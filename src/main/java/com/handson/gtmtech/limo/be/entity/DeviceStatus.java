package com.handson.gtmtech.limo.be.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.OffsetDateTime;

@Entity
@Table(name = "device_status")
@Data
public class DeviceStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private OffsetDateTime timestamp;
    private String deviceId; // device_id
    private Integer batteryLevel; // battery_level
    private String currentStatus; // current_status
    private String lastCommandId; // last_command_id
    private Integer filterLifeRemaining; // filter_life_remaining
    private Double purifiedVolumeLiters; // purified_volume_liters
    private Double latitude;
    private Double longitude;
    private String errorCode; // error_code
    private OffsetDateTime recordedAt; // recorded_at

    @PrePersist
    protected void onCreate() {
        if (recordedAt == null) {
            recordedAt = OffsetDateTime.now();
        }
    }
}
