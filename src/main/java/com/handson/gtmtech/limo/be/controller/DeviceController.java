package com.handson.gtmtech.limo.be.controller;

import com.handson.gtmtech.limo.be.entity.Device;
import com.handson.gtmtech.limo.be.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @GetMapping
    public List<Device> getAllDevices() {
        return deviceService.getAllDevices();
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<Device> getDeviceById(@PathVariable String deviceId) {
        return deviceService.getDeviceById(deviceId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Device createDevice(@RequestBody Device device) {
        return deviceService.createDevice(device);
    }

    @PutMapping("/{deviceId}")
    public ResponseEntity<Device> updateDevice(@PathVariable String deviceId, @RequestBody Device deviceDetails) {
        try {
            Device updatedDevice = deviceService.updateDevice(deviceId, deviceDetails);
            return ResponseEntity.ok(updatedDevice);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{deviceId}")
    public ResponseEntity<Void> deleteDevice(@PathVariable String deviceId) {
        deviceService.deleteDevice(deviceId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/type/{deviceType}")
    public List<Device> getDevicesByType(@PathVariable String deviceType) {
        return deviceService.getDevicesByType(deviceType);
    }
    
}
