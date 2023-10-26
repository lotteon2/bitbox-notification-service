package com.bitbox.notification.controller;

import com.bitbox.notification.entity.Notification;
import com.bitbox.notification.service.NotificationService;
import com.bitbox.notification.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final SseService sseService;
    private final NotificationService notificationService;

    @GetMapping("")
    public ResponseEntity<List<Notification>> getRecentNotifications(@RequestHeader String memberId) {
        return ResponseEntity.ok(notificationService.getRecentNotifications(memberId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Notification>> getAllNotifications(@RequestHeader String memberId) {
        return ResponseEntity.ok(notificationService.getAllNotifications(memberId));
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getUnreadNotificationCount(@RequestHeader String memberId) {
        return ResponseEntity.ok(notificationService.countUnreadNotifications(memberId));
    }

    // TODO : Last-Event-ID
    @GetMapping(value = "/subscription", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public SseEmitter subscribeNotification(@RequestHeader String memberId) {
        return sseService.subscribe(memberId);
    }

    @DeleteMapping("")
    public ResponseEntity<Void> deleteAllNotifications(@RequestHeader String memberId) {
        notificationService.updateNotificationsDeleted(memberId);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@RequestHeader String memberId,
                                                @PathVariable Long notificationId) {
        notificationService.updateNotificationDeleted(memberId, notificationId);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("")
    public ResponseEntity<Void> readAllNotifications(@RequestHeader String memberId) {
        notificationService.updateNotificationsRead(memberId);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/{notificationId}")
    public ResponseEntity<Void> readNotification(@RequestHeader String memberId,
                                              @PathVariable Long notificationId) {
        notificationService.updateNotificationRead(memberId, notificationId);
        return ResponseEntity.accepted().build();
    }
}
