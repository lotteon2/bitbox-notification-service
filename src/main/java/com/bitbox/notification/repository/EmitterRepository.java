package com.bitbox.notification.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {
    SseEmitter save(String emitterId, SseEmitter sseEmitter);
    void saveEventCache(String emitterId, Object event);
    Map<String, SseEmitter> findAllEmitterStartWithMemberId(String memberId);
    Map<String, Object> findAllEventCacheStartWithMemberId(String memberId);
    void deleteByEmitterId(String emitterId);
    void deleteAllEmittersStartWithMemberId(String memberId);
    void deleteAllEventCacheStartWithMemberId(String memberId);
}
