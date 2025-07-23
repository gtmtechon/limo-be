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
    private OffsetDateTime ttimestamp;
    @Column(name = "device_id") // DB 컬럼명과 매핑
    private String deviceId; // device_id
    @Column(name = "battery_level") // DB 컬럼명과 매핑    
    private Integer batteryLevel; // battery_level
    @Column(name = "current_status") // DB 컬럼명과 매핑    
    private String currentStatus; // current_status
    @Column(name = "last_command_id") // DB 컬럼명과 매핑
    private String lastCommandId; // last_command_id
    @Column(name = "filter_life_remaining") // DB 컬럼명과 매핑
    private Integer filterLifeRemaining; // filter_life_remaining
    @Column(name = "purified_volume_liters") // DB 컬럼명과 매핑    
    private Double purifiedVolumeLiters; // purified_volume_liters
    private Double latitude;
    private Double longitude;
    @Column(name = "error_code") // DB 컬럼명과 매핑
    private String errorCode; // error_code
    @Column(name = "recorded_at") // DB 컬럼명과 매핑
    private OffsetDateTime recordedAt; // recorded_at

    private String xprops; // xprops

    @PrePersist
    protected void onCreate() {
        if (recordedAt == null) {
            recordedAt = OffsetDateTime.now();
        }
    }
}
