package com.handson.gtmtech.limo.be.entity;

import lombok.Data;

@Data
public class RobotStatus {
    private String ttimestamp;
    private String deviceId;
    private String type;
    private int batteryLevel;
    private String currentStatus;
    private PurificationStatus purificationStatus; 
    private Location location;

    @Data
    public static class PurificationStatus {
        private int filterLifeRemaining;
        private double purifiedVolumeLiters;
    }

    @Data
    public static class Location {
        private double latitude;
        private double longitude;
    }
}
