package com.handson.gtmtech.limo.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.handson.gtmtech.limo.be.entity.LakeStatus;

import java.time.OffsetDateTime;
import java.util.List;


@Repository
public interface LakeStatusRepository extends JpaRepository<LakeStatus, Long> {                                                                                                                 
    // 특정 센서의 최근 1시간 데이터 조회
    List<LakeStatus> findBySensorIdAndTtimestampBetweenOrderByTtimestampAsc(String sensorId, OffsetDateTime start, OffsetDateTime end);

    // 각 센서별 최신 데이터 조회 (가장 최근 타임스탬프 기준)
    @Query(value = "SELECT ls.* FROM lake_status ls " +
                   "INNER JOIN (SELECT sensor_id, MAX(ttimestamp) as max_timestamp FROM lake_status GROUP BY sensor_id) AS latest " +
                   "ON ls.sensor_id = latest.sensor_id AND ls.ttimestamp = latest.max_timestamp",
            nativeQuery = true)
    List<LakeStatus> findLatestLakeStatusForEachSensor();
}
