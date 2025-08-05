package com.handson.gtmtech.limo.be.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.handson.gtmtech.limo.be.controller.DeviceController;
import com.handson.gtmtech.limo.be.entity.RobotStatus;
import com.handson.gtmtech.limo.be.repository.RobotStatusRepository;


@Service
public class RobotStatusService {

    private final RobotStatusRepository robotStatusRepository;
    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    public RobotStatusService(RobotStatusRepository robotStatusRepository) {
        this.robotStatusRepository = robotStatusRepository;
    }

    /**
     * Redis에서 모든 로봇의 최신 상태를 조회합니다.
     * 이 메서드는 폴링 기반의 REST API에서 사용될 것입니다.
     *
     * @return 모든 로봇의 최신 상태 정보를 담고 있는 리스트
     */
    public List<RobotStatus> getAllLatestRobotStatus() {
        return robotStatusRepository.findAllLatestRobotStatuses();
    }
}
