package com.handson.gtmtech.limo.be.controller;

import com.handson.gtmtech.limo.be.entity.RobotStatus;
import com.handson.gtmtech.limo.be.service.RobotStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/latest-robot-status")
public class RobotStatusController {

    // 로깅을 위한 Logger 인스턴스
    private static final Logger logger = LoggerFactory.getLogger(RobotStatusController.class);

    private final RobotStatusService robotStatusService;

    @Autowired
    public RobotStatusController(RobotStatusService robotStatusService) {
        this.robotStatusService = robotStatusService;
    }

    /**
     * 모든 로봇의 최신 상태를 반환하는 GET API입니다.
     * Service 레이어에서 데이터를 조회하고, 예외 발생 시 적절한 HTTP 응답을 반환합니다.
     *
     * @return 로봇 상태 정보 리스트가 포함된 ResponseEntity
     */
    @GetMapping("/robots")
    public ResponseEntity<List<RobotStatus>> getAllLatestRobotStatuses() {
        try {
            List<RobotStatus> statuses = robotStatusService.getAllLatestRobotStatus();
            // 데이터가 없는 경우를 처리하여 HTTP 200 OK와 함께 빈 리스트를 반환합니다.
            if (statuses.isEmpty()) {
                logger.info("No robot status data found.");
                return ResponseEntity.ok(Collections.emptyList());
            }
            return ResponseEntity.ok(statuses);
        } catch (Exception e) {
            // 예외 발생 시 logger.error를 사용하여 로그를 기록하고, HTTP 500 Internal Server Error를 반환합니다.
            logger.error("Error fetching latest robot statuses: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}