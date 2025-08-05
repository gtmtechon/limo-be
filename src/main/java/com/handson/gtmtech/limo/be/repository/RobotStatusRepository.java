package com.handson.gtmtech.limo.be.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.handson.gtmtech.limo.be.entity.RobotStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class RobotStatusRepository {

    // 로깅을 위한 Logger 인스턴스
    private static final Logger logger = LoggerFactory.getLogger(RobotStatusRepository.class);

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public RobotStatusRepository(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Redis에서 모든 로봇의 최신 상태 정보를 조회합니다.
     * 키 패턴 "robot_status:*"에 해당하는 모든 데이터를 가져와 List<RobotStatus> 형태로 반환합니다.
     *
     * @return 모든 로봇의 최신 상태 정보 리스트. 조회된 데이터가 없으면 빈 리스트를 반환합니다.
     */
    public List<RobotStatus> findAllLatestRobotStatuses() {
        try {
            // Redis에서 "robot_status:*" 패턴에 해당하는 모든 키를 조회합니다.
            Set<String> keys = redisTemplate.keys("robot_status:*");
            if (keys == null || keys.isEmpty()) {
                return new ArrayList<>();
            }

            // 조회된 키들에 해당하는 값(JSON 문자열)들을 한 번에 가져옵니다.
            List<String> rawData = redisTemplate.opsForValue().multiGet(keys);
            if (rawData == null) {
                return new ArrayList<>();
            }

            // JSON 문자열 리스트를 RobotStatus 객체 리스트로 변환합니다.
            return rawData.stream()
                .filter(Objects::nonNull) // null 값은 필터링합니다.
                .map(data -> {
                    try {
                        // 사용자님이 제공한 RobotStatus 엔티티 클래스를 사용하여 JSON을 객체로 변환합니다.
                        return objectMapper.readValue(data, RobotStatus.class);
                    } catch (Exception e) {
                        // JSON 파싱 오류 발생 시 로그를 기록하고 null을 반환하여 이후 필터링합니다.
                        logger.error("Failed to parse Redis data: {}, Error: {}", data, e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull) // 파싱 실패한 객체는 최종 리스트에서 제외합니다.
                .collect(Collectors.toList());

        } catch (Exception e) {
            // Redis 조회 중 발생할 수 있는 예외를 처리합니다.
            logger.error("Error while fetching robot status from Redis: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
}