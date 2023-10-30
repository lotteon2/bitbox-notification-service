package com.bitbox.notification.service;

import com.bitbox.notification.entity.Notification;
import com.bitbox.notification.exception.CustomNotFoundException;
import com.bitbox.notification.exception.ForbiddenException;
import com.bitbox.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<Notification> getRecentNotifications(String memberId) {
        return notificationRepository.getTop5ByMemberIdAndDeletedIsFalseOrderByCreatedAtDesc(memberId);
    }

    public List<Notification> getAllNotifications(String memberId) {
        return notificationRepository.getByMemberIdAndDeletedIsFalse(memberId);
    }

    public Integer countUnreadNotifications(String memberId) {
        return notificationRepository.countByMemberIdAndReadIsFalseAndDeletedIsFalse(memberId);
    }

    @Transactional
    public void updateNotificationsRead(String memberId) {
        notificationRepository.checkNotificationsRead(memberId);
    }

    @Transactional
    public void updateNotificationRead(String memberId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new CustomNotFoundException("존재하지 않는 알림입니다"));
        if(!notification.getMemberId().equals(memberId)) throw new ForbiddenException("권한이 없습니다");
        notificationRepository.save(notification.toBuilder().read(true).build());
    }

    @Transactional
    public void updateNotificationsDeleted(String memberId) {
        notificationRepository.checkNotificationsDeleted(memberId);
    }

    @Transactional
    public void updateNotificationDeleted(String memberId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new CustomNotFoundException("존재하지 않는 알림입니다"));
        if(!notification.getMemberId().equals(memberId)) throw new ForbiddenException("권한이 없습니다");
        notificationRepository.save(notification.toBuilder().deleted(true).build());
    }
}
