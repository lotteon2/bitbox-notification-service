package com.bitbox.notification.repository;

import com.bitbox.notification.entity.Notification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, Long> {
    List<Notification> getTop5ByMemberIdAndDeletedIsFalseOrderByCreatedAtDesc(String memberId);
    List<Notification> getByMemberIdAndDeletedIsFalse(String memberId);
    Integer countByMemberIdAndReadIsFalseAndDeletedIsFalse(String memberId);

    /**
     여러건의 벌크 연산을 수행할 경우 영속성 컨텍스트에 있는 1차 캐시를 통하지 않고 DB에 바로 쿼리를 실행하려 하기 때문에
     예외가 발생하게 된다. @Modifying(clearAutomatically = true)를 통해 1차 캐시를 비우고 벌크 연산을 DB에 직접 실행할 수 있다.
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Notification as noti SET noti.read = true WHERE noti.memberId = :memberId")
    void checkNotificationsRead(String memberId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Notification as noti SET noti.deleted = true WHERE noti.memberId = :memberId")
    void checkNotificationsDeleted(String memberId);
}
