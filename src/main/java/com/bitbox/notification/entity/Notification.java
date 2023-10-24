package com.bitbox.notification.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@DynamicInsert
@DynamicUpdate
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Column(nullable = false)
    private String memberId;

    @Column(nullable = false)
    private String notificationInfo;

    @Column(nullable = false)
    private String notificationLink;

    @Column(nullable = false, updatable = false, columnDefinition = "DATETIME default NOW()")
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "is_read", columnDefinition = "boolean default false")
    private boolean read;

    @Column(nullable = false, name = "is_deleted", columnDefinition = "boolean default false")
    private boolean deleted;
}
