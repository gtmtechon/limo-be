package com.handson.gtmtech.limo.be.controller;

import com.handson.gtmtech.limo.be.entity.LakeStatus;
import com.handson.gtmtech.limo.be.service.LakeStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lake-status")
public class LakeStatusController {
    @Autowired
    private LakeStatusService lakeStatusService;

    @GetMapping
    public List<LakeStatus> getLatestLakeStatus() {
        return lakeStatusService.getLatestLakeStatus();
    }

    @GetMapping("/all")
    public List<LakeStatus> getAllLakeStatus() {
        return lakeStatusService.getAllLakeStatus();
    }

    @GetMapping("/hourly")
    public List<LakeStatus> getHourlyLakeStatus(@RequestParam String sensorId) {
        return lakeStatusService.getHourlyLakeStatus(sensorId);
    }

    @PostMapping
    public LakeStatus createLakeStatus(@RequestBody LakeStatus lakeStatus) {
        return lakeStatusService.saveLakeStatus(lakeStatus);
    }
}
