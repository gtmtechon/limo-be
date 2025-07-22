package com.handson.gtmtech.limo.be.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.handson.gtmtech.limo.be.entity.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {
    List<Device> findByDeviceType(String deviceType);
}