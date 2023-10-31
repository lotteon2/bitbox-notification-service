package com.bitbox.notification.dto;

import io.github.bitbox.bitbox.dto.NotificationDto;
import lombok.Getter;

@Getter
public class NotificationTuple {
    private final String notificationLink;
    private final String notificationInfo;

    private NotificationTuple(String notificationLink, String notificationInfo) {
        this.notificationLink = notificationLink;
        this.notificationInfo = notificationInfo;
    }

    public static NotificationTuple strategies(NotificationDto notificationDto) {
        String notificationInfo = null;
        String notificationLink = null;
        switch (notificationDto.getNotificationType()) { // TODO : url들 확인하고 환경 변수로 빼야할 듯?
            case COMMENT:
                notificationInfo = notificationDto.getSenderNickname() + "님이 댓글을 달았습니다";
                notificationLink = "https://ephemeral-valkyrie-b2a791.netlify.app/board-service/board/" + notificationDto.getBoardId();
                break;

            case ATTENDANCE:
                notificationInfo = "출석 10분 전입니다.";
                notificationLink = "https://ephemeral-valkyrie-b2a791.netlify.app/mypage";
                break;

            case SUBSCRIPTION:
                notificationInfo = "정액권 마감 10분 전입니다.";
                notificationLink = "https://ephemeral-valkyrie-b2a791.netlify.app/payment";
                break;
        }

        return new NotificationTuple(notificationLink, notificationInfo);
    }
}
