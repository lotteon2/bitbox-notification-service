package com.bitbox.notification.repository;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class EmitterRepositoryImpl implements EmitterRepository {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    @Override
    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId, sseEmitter);
        return sseEmitter;
    }

    @Override
    public void saveEventCache(String eventCacheId, Object event) {
        eventCache.put(eventCacheId, event);
    }

    @Override
    public Map<String, SseEmitter> findAllEmitterStartWithMemberId(String memberId) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(memberId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<String, Object> findAllEventCacheStartWithMemberId(String memberId) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(memberId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void deleteByEmitterId(String emitterId) {
        emitters.remove(emitterId);
    }

    @Override
    public void deleteAllEmittersStartWithMemberId(String memberId) {
        emitters.forEach((key, value) -> {
            if(key.startsWith(memberId)) {
                emitters.remove(key);
            }
        });
    }

    @Override
    public void deleteAllEventCacheStartWithMemberId(String memberId) {
        eventCache.forEach((key, value) -> {
            if(key.startsWith(memberId)) {
                eventCache.remove(key);
            }
        });
    }
}
