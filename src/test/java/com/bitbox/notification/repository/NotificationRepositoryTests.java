package com.bitbox.notification.repository;

import com.bitbox.notification.entity.Notification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class NotificationRepositoryTests {
    @Autowired
    NotificationRepository notificationRepository;

    @Test
    @DisplayName("갯수가 5개 이하라면 갯수만큼, 5개 초과라면 5개만 조회해야 한다")
    void getTop5Test() {

        final String testMember1 = "random_member_uuid1";
        final String testMember2 = "random_member_uuid2";

        List<Notification> testList1 = new ArrayList<>();
        for(int i=0; i<6; i++) {
            testList1.add(Notification.builder()
                    .notificationInfo(String.format("notification info: %d", i))
                    .memberId(testMember1)
                    .notificationLink(String.format("notification link: %d", i))
                    .build());
        }
        notificationRepository.saveAll(testList1);

        List<Notification> testList2 = new ArrayList<>();
        for(int i=0; i<4; i++) {
            testList2.add(Notification.builder()
                    .notificationInfo(String.format("notification info: %d", i))
                    .memberId(testMember2)
                    .notificationLink(String.format("notification link: %d", i))
                    .build());
        }
        notificationRepository.saveAll(testList2);

        List<Notification> list1 = notificationRepository.getTop5ByMemberIdAndDeletedIsFalseOrderByCreatedAtDesc(testMember1);
        List<Notification> list2 = notificationRepository.getTop5ByMemberIdAndDeletedIsFalseOrderByCreatedAtDesc(testMember2);

        assert list1.size() == 5;
        assert list2.size() == 4;
    }

    @Test
    @DisplayName("5개를 생성하면 갯수를 셌을 때 5개여야 한다")
    void getCountTest() {
        final String testMember1 = "random_member_uuid1";
        final String testMember2 = "random_member_uuid2";

        List<Notification> testList1 = new ArrayList<>();
        for(int i=0; i<5; i++) {
            testList1.add(Notification.builder()
                    .notificationInfo(String.format("notification info: %d", i))
                    .memberId(testMember1)
                    .notificationLink(String.format("notification link: %d", i))
                    .build());
        }
        notificationRepository.saveAll(testList1);

        List<Notification> testList2 = new ArrayList<>();
        for(int i=0; i<3; i++) {
            testList2.add(Notification.builder()
                    .notificationInfo(String.format("notification info: %d", i))
                    .memberId(testMember2)
                    .notificationLink(String.format("notification link: %d", i))
                    .build());
        }
        notificationRepository.saveAll(testList2);

        assert notificationRepository.countByMemberIdAndReadIsFalseAndDeletedIsFalse(testMember1) == 5;
        assert notificationRepository.countByMemberIdAndReadIsFalseAndDeletedIsFalse(testMember2) == 3;
    }

    @Test
    @DisplayName("memberId로 is_read를 bulk update하고 나면 is_read가 false인 row가 없어야 한다")
    void bulkUpdateReadTest() {
        final String testMember = "random_member_uuid";

        List<Notification> testList = new ArrayList<>();
        for(int i=0; i<5; i++) {
            testList.add(Notification.builder()
                    .notificationInfo(String.format("notification info: %d", i))
                    .memberId(testMember)
                    .notificationLink(String.format("notification link: %d", i))
                    .build());
        }
        notificationRepository.saveAll(testList);

        notificationRepository.checkNotificationsRead(testMember);

        assert notificationRepository.countByMemberIdAndReadIsFalseAndDeletedIsFalse(testMember) == 0;
    }

    @Test
    @DisplayName("memberId로 is_deleted를 bulk update하고 나면 is_deleted가 false인 row가 없어야 한다")
    void bulkUpdateDeletedTest() {
        final String testMember = "random_member_uuid";

        List<Notification> testList = new ArrayList<>();
        for(int i=0; i<5; i++) {
            testList.add(Notification.builder()
                    .notificationInfo(String.format("notification info: %d", i))
                    .memberId(testMember)
                    .notificationLink(String.format("notification link: %d", i))
                    .build());
        }
        notificationRepository.saveAll(testList);

        notificationRepository.checkNotificationsDeleted(testMember);

        assert notificationRepository.getByMemberIdAndDeletedIsFalse(testMember).isEmpty();
    }
}
