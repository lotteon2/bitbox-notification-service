package com.bitbox.notification.service;

import com.bitbox.notification.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SseService {

    private final EmitterRepository emitterRepository;

    private final long TIME_OUT = 60L * 60L * 1000L;

    private final String WELCOME_MESSAGE = "WELCOME TO BITBOX";

    public SseEmitter subscribe(String memberId) {
        String emitterId = makeTimeIncludeId(memberId);
        SseEmitter sseEmitter = emitterRepository.save(emitterId, new SseEmitter(TIME_OUT));

        // call-back
        sseEmitter.onCompletion(() -> emitterRepository.deleteByEmitterId(emitterId));
        sseEmitter.onTimeout(() -> emitterRepository.deleteByEmitterId(emitterId));

        String eventId = makeTimeIncludeId(memberId);
        sendNotification(sseEmitter, eventId, emitterId, WELCOME_MESSAGE);

        return sseEmitter;
    }

    private String makeTimeIncludeId(String memberId) {
        return memberId + "_" + System.currentTimeMillis();
    }

    private void sendNotification(SseEmitter sseEmitter, String eventId, String emitterId, Object notification) {
        try {
            sseEmitter.send(SseEmitter.event().id(eventId).data(notification));
        } catch (IOException e) {
            emitterRepository.deleteByEmitterId(emitterId);
        }
    }
}
