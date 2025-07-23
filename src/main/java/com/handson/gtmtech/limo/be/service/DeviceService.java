package com.handson.gtmtech.limo.be.service;

import com.handson.gtmtech.limo.be.entity.Device;
import com.handson.gtmtech.limo.be.entity.DeviceStatus;
import com.handson.gtmtech.limo.be.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceStatusService deviceStatusService; // DeviceStatusService 주입

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    public Optional<Device> getDeviceById(String deviceId) {
        return deviceRepository.findById(deviceId);
    }

    public Device createDevice(Device device) {
        // 1. Device를 먼저 저장
        Device savedDevice = deviceRepository.save(device);

        // 2. DeviceStatus 테이블에 "등록됨" 상태 추가
        DeviceStatus deviceStatus = new DeviceStatus();
        deviceStatus.setCurrentStatus("registered"); // 상태를 "등록됨"으로 설정
        deviceStatus.setTtimestamp(OffsetDateTime.now()); // 현재 시간 설정
        deviceStatus.setDeviceId(savedDevice.getDeviceId()); // 저장된 Device의 ID 설정
        deviceStatus.setRecordedAt(OffsetDateTime.now()); // 현재 시간 설정     

        // 다른 필드 (예: battery_level, filter_life_remaining 등)는
        // 초기 등록 시에는 null 또는 기본값으로 설정할 수 있습니다.
        // deviceStatus.setBatteryLevel(null);
        // deviceStatus.setFilterLifeRemaining(null);
        // deviceStatus.setPurifiedVolumeLitters(null);
         deviceStatus.setLatitude(savedDevice.getLatitude()); // 장비의 위도/경도 정보도 함께 저장 가능
         deviceStatus.setLongitude(savedDevice.getLongitude());
        // deviceStatus.setErrorCode(null);

        deviceStatusService.saveDeviceStatus(deviceStatus); // DeviceStatus 저장

        return savedDevice;
    }


    public Device updateDevice(String deviceId, Device deviceDetails) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("Device not found with id " + deviceId));
        device.setDeviceName(deviceDetails.getDeviceName());
        device.setDeviceType(deviceDetails.getDeviceType());
        device.setOwner(deviceDetails.getOwner());
        device.setLatitude(deviceDetails.getLatitude());
        device.setLongitude(deviceDetails.getLongitude());
        device.setCoordSystem(deviceDetails.getCoordSystem());
        device.setStatus(deviceDetails.getStatus());
        device.setDescription(deviceDetails.getDescription());
        return deviceRepository.save(device);
    }

    public void deleteDevice(String deviceId) {
        deviceRepository.deleteById(deviceId);
    }

    public List<Device> getDevicesByType(String deviceType) {
        return deviceRepository.findByDeviceType(deviceType);
    }
}