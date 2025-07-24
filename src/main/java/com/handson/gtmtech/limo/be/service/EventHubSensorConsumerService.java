package com.handson.gtmtech.limo.be.service;

import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubConsumerAsyncClient;
import com.azure.messaging.eventhubs.EventProcessorClient;
import com.azure.messaging.eventhubs.EventProcessorClientBuilder;
import com.azure.messaging.eventhubs.checkpointstore.blob.BlobCheckpointStore;
import com.azure.messaging.eventhubs.models.ErrorContext;
import com.azure.messaging.eventhubs.models.EventContext;
import com.azure.storage.blob.BlobContainerAsyncClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.handson.gtmtech.limo.be.entity.LakeStatus; // LakeStatus 엔티티 임포트
import com.handson.gtmtech.limo.be.entity.DeviceStatus; // DeviceStatus 엔티티 임포트 (필요시)
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class EventHubSensorConsumerService {

   
    private static final Logger logger = LoggerFactory.getLogger(EventHubSensorConsumerService.class);

    @Value("${azure.eventhubs.connection.string}")
    private String eventHubConnectionString;

    @Value("${azure.eventhubs.name}")
    private String eventHubName;

    @Value("${azure.eventhubs.consumer.group}")
    private String consumerGroup;

    // Azure Blob Storage 설정
    @Value("${azure.storage.account.name}") // 계정 이름 주입
    private String blobAccountName;

    @Value("${azure.storage.sas.token}") // SAS 토큰 주입
    private String blobSasToken;

    @Value("${azure.storage.blob.container.eventhub.checkpoint.name}")
    private String blobContainerName;

    @Autowired
    private LakeStatusService lakeStatusService; // LakeStatus 저장 서비스

    @Autowired
    private DeviceStatusService deviceStatusService; // DeviceStatus 저장 서비스 (필요시)

    private EventProcessorClient eventProcessorClient;
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());


    @PostConstruct
    public void startEventHubConsumer() {
        logger.info("Starting Event Hub Consumer...");

        // SAS 토큰 기반으로 BlobContainerAsyncClient 생성
        // 엔드포인트 URL 구성
        String blobServiceEndpoint = String.format("https://%s.blob.core.windows.net", blobAccountName);

        BlobContainerAsyncClient blobContainerAsyncClient = new BlobContainerClientBuilder()
            .endpoint(blobServiceEndpoint) // 서비스 엔드포인트 설정
            .sasToken(blobSasToken)       // SAS 토큰 설정
            .containerName(blobContainerName)
            .buildAsyncClient();

        // Blob Storage 컨테이너가 없으면 생성
        blobContainerAsyncClient.exists().flatMap(exists -> {
            if (!exists) {
                logger.info("Blob container {} does not exist. Creating it.", blobContainerName);
                return blobContainerAsyncClient.create();
            } else {
                return reactor.core.publisher.Mono.empty();
            }
        }).block(); // 블로킹 호출로 컨테이너 생성 확인

        eventProcessorClient = new EventProcessorClientBuilder()
            .connectionString(eventHubConnectionString, eventHubName)
            .consumerGroup(consumerGroup)
            .processEvent(this::processEvent) // 이벤트 처리 핸들러
            .processError(this::processError)   // 오류 처리 핸들러
            .checkpointStore(new BlobCheckpointStore(blobContainerAsyncClient)) // 체크포인트 저장소
            .buildEventProcessorClient();

        eventProcessorClient.start();
        logger.info("Event Hub Consumer started.");
    }

    // Event Hub에서 메시지가 도착했을 때 호출되는 메서드
    private void processEvent(EventContext eventContext) {
        String eventBody = eventContext.getEventData().getBodyAsString();
        logger.info("Received event from partition {} with sequence number {}: {}",
            eventContext.getPartitionContext().getPartitionId(),
            eventContext.getEventData().getSequenceNumber(),
            eventBody);

        try {
            // 여기에서 Event Hub 메시지 내용을 파싱하고 DB에 저장합니다.
            // 메시지 형식에 따라 LakeStatus 또는 DeviceStatus로 파싱합니다.

            if (eventBody.contains("\"type\": \"lakeStatus\"")) {
                LakeStatus lakeStatus = objectMapper.readValue(eventBody, LakeStatus.class);
                lakeStatusService.saveLakeStatus(lakeStatus);
                logger.info("Saved LakeStatus: {}", lakeStatus);
            } else if (eventBody.contains("\"type\": \"deviceStatus\"")) {
                DeviceStatus deviceStatus = objectMapper.readValue(eventBody, DeviceStatus.class);
                deviceStatusService.saveDeviceStatus(deviceStatus);
                logger.info("Saved DeviceStatus: {}", deviceStatus);
            } else {
                logger.warn("Unknown event type received: {}", eventBody);
            }

            // 처리 완료 후 체크포인트 업데이트 (중복 처리 방지)
            eventContext.updateCheckpoint();
        } catch (IOException e) {
            logger.error("Error parsing or saving event: {}", eventBody, e);
            // 파싱 오류 시 메시지를 스킵하거나 데드 레터 큐로 보낼 수 있습니다.
        } catch (Exception e) {
            logger.error("Error processing event: {}", eventBody, e);
            // DB 저장 오류 등 기타 예외 처리
        }
    }

    // Event Hub 처리 중 오류 발생 시 호출되는 메서드
    private void processError(ErrorContext errorContext) {
        logger.error("Error occurred while processing events from partition {}. Error: {}",
            errorContext.getPartitionContext().getPartitionId(),
            errorContext.getThrowable().getMessage(),
            errorContext.getThrowable());
    }

    @PreDestroy
    public void stopEventHubConsumer() {
        if (eventProcessorClient != null) {
            eventProcessorClient.stop();
            logger.info("Event Hub Consumer stopped.");
        }
    }
}