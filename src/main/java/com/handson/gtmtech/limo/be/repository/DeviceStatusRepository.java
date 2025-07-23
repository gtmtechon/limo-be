package com.handson.gtmtech.limo.be.repository;

import com.handson.gtmtech.limo.be.entity.DeviceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceStatusRepository extends JpaRepository<DeviceStatus, Long> {
    // 각 장치별 최신 데이터 조회 (가장 최근 타임스탬프 기준)
    @Query(value = "SELECT ds.* FROM device_status ds " +
                   "INNER JOIN (SELECT device_id, MAX(ttimestamp) as max_timestamp FROM device_status GROUP BY device_id) AS latest " +
                   "ON ds.device_id = latest.device_id AND ds.ttimestamp = latest.max_timestamp",
            nativeQuery = true)
    List<DeviceStatus> findLatestDeviceStatusForEachDevice();

    // 특정 장치 타입의 최신 상태 조회 (예: water-purifier-robot)
    @Query(value = "SELECT ds.* FROM device_status ds " +
                   "INNER JOIN (SELECT device_id, MAX(ttimestamp) as max_timestamp FROM device_status GROUP BY device_id) AS latest " +
                   "ON ds.device_id = latest.device_id AND ds.ttimestamp = latest.max_timestamp " +
                   "INNER JOIN devices d ON ds.device_id = d.device_id " +
                   "WHERE d.device_type = :deviceType",
            nativeQuery = true)
    List<DeviceStatus> findLatestDeviceStatusByDeviceType(String deviceType);
}