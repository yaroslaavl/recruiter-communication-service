package org.yaroslaavl.communicationservice.broker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommunicationEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.communication.exchange}")
    public String exchange;

    @Value("${rabbitmq.communication.queues.start.routing-key}")
    public String routingKey;

    public void publishCommunicationEvent(NotificationDto notificationDto) {
        log.info("Publishing communication event for notification {}", notificationDto);
        rabbitTemplate.convertAndSend(exchange, routingKey, notificationDto);
    }

    public static NotificationDto startChat(String candidateId, String fullName) {
        return NotificationDto.builder()
                .targetUserId(candidateId)
                .entityType("SYSTEM")
                .notificationType("EMAIL")
                .content("chat_start")
                .contentVariables(Map.of(
                        "fullName", fullName))
                .build();
    }

    public static NotificationDto videoPin(String candidateId, String fullName) {
        return NotificationDto.builder()
                .targetUserId(candidateId)
                .entityType("SYSTEM")
                .notificationType("EMAIL")
                .content("video_pin")
                .contentVariables(Map.of(
                        "fullName", fullName))
                .build();
    }
}
