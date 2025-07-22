package com.handson.gtmtech.limo.be.controller;

import com.handson.gtmtech.limo.be.entity.DeviceStatus;
import com.handson.gtmtech.limo.be.service.DeviceStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/device-status")
public class DeviceStatusController {
    @Autowired
    private DeviceStatusService deviceStatusService;

    @GetMapping
    public List<DeviceStatus> getLatestDeviceStatus(@RequestParam(required = false) String deviceType) {
        return deviceStatusService.getLatestDeviceStatus(deviceType);
    }

    @PostMapping
    public DeviceStatus createDeviceStatus(@RequestBody DeviceStatus deviceStatus) {
        return deviceStatusService.saveDeviceStatus(deviceStatus);
    }

    // 로봇 위치 정보를 위한 API (최신 로봇 상태에서 위치 추출)
    @GetMapping("/robot-location")
    public List<DeviceStatus> getRobotLocations() {
        // deviceType을 "water-purifier-robot"으로 필터링하여 최신 상태를 가져옴
        return deviceStatusService.getLatestDeviceStatus("water-purifier-robot");
    }
}