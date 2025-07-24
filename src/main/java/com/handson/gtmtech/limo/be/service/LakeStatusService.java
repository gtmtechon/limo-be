package com.handson.gtmtech.limo.be.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.handson.gtmtech.limo.be.entity.LakeStatus;
import com.handson.gtmtech.limo.be.repository.LakeStatusRepository;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class LakeStatusService {
    @Autowired
    private LakeStatusRepository lakeStatusRepository;

    public List<LakeStatus> getAllLakeStatus() {
        return lakeStatusRepository.findAll();
    }

    public LakeStatus saveLakeStatus(LakeStatus lakeStatus) {
        // 오염도 로직 (예시: pH, 탁도, 용존 산소 기반)
        if (lakeStatus.getPh() != null && lakeStatus.getTurbidity() != null && lakeStatus.getDissolvedOxygen() != null) {
            if (lakeStatus.getPh() >= 6.5 && lakeStatus.getPh() <= 8.5 &&
                lakeStatus.getTurbidity() < 20 && lakeStatus.getDissolvedOxygen() > 5) {
                lakeStatus.setPollutionLevel("Good");
            } else if (lakeStatus.getPh() >= 6.0 && lakeStatus.getPh() <= 9.0 &&
                       lakeStatus.getTurbidity() < 50 && lakeStatus.getDissolvedOxygen() > 3) {
                lakeStatus.setPollutionLevel("Moderate");
            } else {
                lakeStatus.setPollutionLevel("Poor");
            }
        } else {
            lakeStatus.setPollutionLevel("Unknown");
        }
        return lakeStatusRepository.save(lakeStatus);
    }

    public List<LakeStatus> getLatestLakeStatus() {
        return lakeStatusRepository.findLatestLakeStatusForEachSensor();
    }

    public List<LakeStatus> getHourlyLakeStatus(String sensorId) {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime oneHourAgo = now.minusHours(1);
        return lakeStatusRepository.findBySensorIdAndTtimestampBetweenOrderByTtimestampAsc(sensorId, oneHourAgo, now);
    }
}
