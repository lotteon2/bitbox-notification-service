package com.bitbox.notification.service;

import com.bitbox.notification.entity.Notification;
import com.bitbox.notification.exception.CustomNotFoundException;
import com.bitbox.notification.exception.ForbiddenException;
import com.bitbox.notification.repository.NotificationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class NotificationServiceTests {

    @Autowired
    NotificationService notificationService;

    @Autowired
    NotificationRepository notificationRepository;

    final String TEST_MEMBER_ID = "asdasdasd-asdasdasd-asdasda";
    final String TEST_OTHER_MEMBER_ID = "asdasdaasd-asdasdasd-asdadsa";
    final Long TEST_NOTIFICATION_ID = 0L;

    final String TEST_INFO = "TEST_INFO";
    final String TEST_URL = "TEST_URL";

    @Test
    @DisplayName("존재하지 않는 알림 읽음 처리 예외 테스트")
    void updateReadNotFoundExceptionTest() {
        Assertions.assertThrows(CustomNotFoundException.class,
                () -> notificationService.updateNotificationRead(TEST_MEMBER_ID, TEST_NOTIFICATION_ID)
        );
    }

    @Test
    @DisplayName("권한 없는 알림 읽음 처리 예외 테스트")
    void updateReadForbiddenExceptionTest() {
        Notification notification = notificationRepository.save(Notification.builder()
                .notificationInfo(TEST_INFO)
                .notificationLink(TEST_URL)
                .memberId(TEST_OTHER_MEMBER_ID)
                .build());

        Assertions.assertThrows(ForbiddenException.class,
                () -> notificationService.updateNotificationRead(TEST_MEMBER_ID, notification.getNotificationId())
        );
    }

    @Test
    @DisplayName("존재하지 않는 알림 삭제 처리 예외 테스트")
    void updateDeletedNotFoundExceptionTest() {
        Assertions.assertThrows(CustomNotFoundException.class,
                () -> notificationService.updateNotificationRead(TEST_MEMBER_ID, TEST_NOTIFICATION_ID)
        );
    }

    @Test
    @DisplayName("권한 없는 알림 읽음 처리 예외 테스트")
    void updateDeletedForbiddenExceptionTest() {
        Notification notification = notificationRepository.save(Notification.builder()
                .notificationInfo(TEST_INFO)
                .notificationLink(TEST_URL)
                .memberId(TEST_OTHER_MEMBER_ID)
                .build());

        Assertions.assertThrows(ForbiddenException.class,
                () -> notificationService.updateNotificationDeleted(TEST_MEMBER_ID, notification.getNotificationId())
        );
    }
}
