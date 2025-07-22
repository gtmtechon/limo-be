package com.handson.gtmtech.limo.be.service;

import com.handson.gtmtech.limo.be.entity.Device;
import com.handson.gtmtech.limo.be.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    public Optional<Device> getDeviceById(String deviceId) {
        return deviceRepository.findById(deviceId);
    }

    public Device createDevice(Device device) {
        return deviceRepository.save(device);
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