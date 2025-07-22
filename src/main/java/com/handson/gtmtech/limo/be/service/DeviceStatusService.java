package com.handson.gtmtech.limo.be.service;

import com.handson.gtmtech.limo.be.entity.DeviceStatus;
import com.handson.gtmtech.limo.be.repository.DeviceStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceStatusService {
    @Autowired
    private DeviceStatusRepository deviceStatusRepository;

    public List<DeviceStatus> getAllDeviceStatus() {
        return deviceStatusRepository.findAll();
    }

    public DeviceStatus saveDeviceStatus(DeviceStatus deviceStatus) {
        return deviceStatusRepository.save(deviceStatus);
    }

    public List<DeviceStatus> getLatestDeviceStatus(String deviceType) {
        if (deviceType != null && !deviceType.isEmpty()) {
            return deviceStatusRepository.findLatestDeviceStatusByDeviceType(deviceType);
        }
        return deviceStatusRepository.findLatestDeviceStatusForEachDevice();
    }
}