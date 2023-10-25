package com.bitbox.notification.repository;

import com.bitbox.notification.entity.Notification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class EmitterRepositoryTests {
    @Autowired
    EmitterRepository emitterRepository;

    private final Long DEFAULT_TIMEOUT = 60L * 1000L * 60L;
    private final String TEST_MEMBER_ID = "absacasdw-asdeherhdfn-asdasv";

    @Test
    @DisplayName("SseEmitter 생성 테스트")
    public void SseEmitterSaveTest() {
        String emitterId = TEST_MEMBER_ID + "_" + System.currentTimeMillis();
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);

        Assertions.assertDoesNotThrow(() -> emitterRepository.save(emitterId, sseEmitter));

        emitterRepository.deleteAllEmittersStartWithMemberId(TEST_MEMBER_ID);
    }

    @Test
    @DisplayName("수신한 이벤트를 캐시에 저장한다")
    public void saveEventCacheTest() {
        String eventCacheId = TEST_MEMBER_ID + "_" + System.currentTimeMillis();
        Notification notification = Notification.builder()
                .notificationLink("NOTIFICATION_LINK")
                .notificationInfo("NOTIFICATION_INFO")
                .memberId(TEST_MEMBER_ID)
                .build();

        Assertions.assertDoesNotThrow(() -> emitterRepository.saveEventCache(eventCacheId, notification));

        emitterRepository.deleteAllEventCacheStartWithMemberId(TEST_MEMBER_ID);
    }

    @Test
    @DisplayName("회원이 접속한 모든 emitter를 찾는다")
    public void findAllEmitterTest() {
        long currTime = System.currentTimeMillis();
        for(int i=0; i<5; i++) {
            String emitterId = TEST_MEMBER_ID + "_" + currTime;
            emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
            currTime += currTime / 2;
        }

        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithMemberId(TEST_MEMBER_ID);

        Assertions.assertEquals(5, emitters.size());

        emitterRepository.deleteAllEmittersStartWithMemberId(TEST_MEMBER_ID);
    }

    @Test
    @DisplayName("회원이 수신한 이벤트를 이벤트캐시에서 모두 찾는다")
    public void findAllEventCacheTest() {
        long currTime = System.currentTimeMillis();
        for(int i=0; i<3; i++) {
            Notification notification = Notification.builder()
                    .notificationLink("NOTIFICATION_LINK")
                    .notificationInfo("NOTIFICATION_INFO")
                    .memberId(TEST_MEMBER_ID)
                    .build();

            String eventCacheId = TEST_MEMBER_ID + "_" + currTime;
            currTime += currTime / 2;

            emitterRepository.saveEventCache(eventCacheId, notification);
        }

        Map<String, Object> events = emitterRepository.findAllEventCacheStartWithMemberId(TEST_MEMBER_ID);

        Assertions.assertEquals(3, events.size());

        emitterRepository.deleteAllEventCacheStartWithMemberId(TEST_MEMBER_ID);
    }

    @Test
    @DisplayName("emitterId를 통해 emitter를 제거한다")
    public void deleteEmitterTest() {
        String emitterId = TEST_MEMBER_ID + "_" + System.currentTimeMillis();
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);

        emitterRepository.save(emitterId, sseEmitter);

        emitterRepository.deleteByEmitterId(emitterId);

        Assertions.assertEquals(0, emitterRepository.findAllEmitterStartWithMemberId(TEST_MEMBER_ID).size());
    }

    @Test
    @DisplayName("사용자의 모든 emitter를 제거한다")
    public void deleteAllEmitterTest() {
        long currTime = System.currentTimeMillis();
        for(int i=0; i<5; i++) {
            String emitterId = TEST_MEMBER_ID + "_" + currTime;
            emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
            currTime += currTime / 2;
        }

        emitterRepository.deleteAllEmittersStartWithMemberId(TEST_MEMBER_ID);

        Assertions.assertEquals(0, emitterRepository.findAllEmitterStartWithMemberId(TEST_MEMBER_ID).size());
    }

    @Test
    @DisplayName("사용자의 모든 event cache를 제거한다")
    public void deleteAllEventCacheTest() {
        for(int i=0; i<3; i++) {
            Notification notification = Notification.builder()
                    .notificationLink("NOTIFICATION_LINK")
                    .notificationInfo("NOTIFICATION_INFO")
                    .memberId(TEST_MEMBER_ID)
                    .build();

            emitterRepository.saveEventCache(TEST_MEMBER_ID, notification);
        }

        emitterRepository.deleteAllEventCacheStartWithMemberId(TEST_MEMBER_ID);

        Assertions.assertEquals(0, emitterRepository.findAllEventCacheStartWithMemberId(TEST_MEMBER_ID).size());
    }
}
