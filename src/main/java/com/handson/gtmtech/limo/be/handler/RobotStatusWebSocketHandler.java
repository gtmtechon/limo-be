// package com.handson.gtmtech.limo.be.handler;

// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.handson.gtmtech.limo.be.service.RobotStatusService;

// import org.springframework.stereotype.Component;
// import org.springframework.web.reactive.socket.WebSocketHandler;
// import org.springframework.web.reactive.socket.WebSocketSession;
// import reactor.core.publisher.Mono;

// @Component
// public class RobotStatusWebSocketHandler implements WebSocketHandler {

//     private final RobotStatusService robotStatusService;
//     private final ObjectMapper objectMapper;

//     public RobotStatusWebSocketHandler(RobotStatusService robotStatusService, ObjectMapper objectMapper) {
//         this.robotStatusService = robotStatusService;
//         this.objectMapper = objectMapper;
//     }

//     @Override
//     public Mono<Void> handle(WebSocketSession session) {
//         // 클라이언트에게 보낼 데이터 스트림을 정의합니다.
//         // Service에서 제공하는 로봇 상태 Flux를 매핑하여 WebSocket 메시지로 변환합니다.
//         // 이 예제에서는 Pub/Sub 리스너가 없으므로 초기 데이터만 보냅니다.
//         // Pub/Sub 리스너를 Service에 추가하여 실시간 업데이트를 처리할 수 있습니다.
//         return session.send(robotStatusService.getLatestRobots()
//                 .map(robotStatus -> {
//                     try {
//                         return objectMapper.writeValueAsString(robotStatus);
//                     } catch (JsonProcessingException e) {
//                         throw new RuntimeException(e);
//                     }
//                 })
//                 .map(session::textMessage)
//         ).log(); // 로그를 추가하여 스트림 동작을 확인합니다.
//     }
// }