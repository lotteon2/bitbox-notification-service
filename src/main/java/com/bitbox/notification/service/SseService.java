package com.bitbox.notification.service;

import com.bitbox.notification.dto.NotificationTuple;
import com.bitbox.notification.entity.Notification;
import com.bitbox.notification.repository.EmitterRepository;
import com.bitbox.notification.repository.NotificationRepository;
import io.github.bitbox.bitbox.dto.NotificationDto;
import io.github.bitbox.bitbox.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SseService {

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    public SseEmitter subscribe(String memberId) {
        final long TIME_OUT = 60L * 60L * 1000L;
        final String WELCOME_MESSAGE = "Hello, BITBOX";
        final String CONNECT_EVENT = "CONNECT";
        String emitterId = makeTimeIncludeId(memberId);
        SseEmitter sseEmitter = emitterRepository.save(emitterId, new SseEmitter(TIME_OUT));

        // call-back
        sseEmitter.onCompletion(() -> emitterRepository.deleteByEmitterId(emitterId));
        sseEmitter.onTimeout(() -> emitterRepository.deleteByEmitterId(emitterId));

        String eventId = makeTimeIncludeId(memberId);
        sendNotification(sseEmitter, eventId, CONNECT_EVENT, emitterId, WELCOME_MESSAGE);

        return sseEmitter;
    }

    private String makeTimeIncludeId(String memberId) {
        return memberId + "_" + System.currentTimeMillis();
    }

    private void sendNotification(SseEmitter sseEmitter, String notificationType, String eventId, String emitterId, Object notificationInfo) {
        try {
            sseEmitter.send(SseEmitter.event().name(notificationType).id(eventId).data(notificationInfo));
        } catch (IOException e) {
            emitterRepository.deleteByEmitterId(emitterId);
        }
    }

    @KafkaListener(topics = "alarmTopic")
    public void send(NotificationDto notificationDto) { // alarmTopic Consume해서

        // NotificationType 따라 url과 info 달라짐.
        NotificationTuple notificationTuple = NotificationTuple.strategies(notificationDto);

        // 해당 사용자의 emitter들에 모두 send
        emitterRepository.findAllEmitterStartWithMemberId(notificationDto.getReceiverId()).forEach(
                (emitterId, sseEmitter) -> sendNotification(
                        sseEmitter,
                        notificationDto.getNotificationType().name(),
                        makeTimeIncludeId(notificationDto.getReceiverId()),
                        emitterId,
                        notificationTuple.getNotificationInfo()
                )
        );

        // db에 알림 저장
        notificationRepository.save(Notification.builder()
                .memberId(notificationDto.getReceiverId())
                .notificationLink(notificationTuple.getNotificationLink())
                .notificationInfo(notificationTuple.getNotificationInfo())
                .build()
        );
    }
}
