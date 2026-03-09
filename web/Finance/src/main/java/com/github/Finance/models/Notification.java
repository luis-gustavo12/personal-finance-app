package com.github.Finance.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Table(name = "notifications")
@Entity
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean seen;

    @Column(name = "seen_at")
    private LocalDateTime seenAt;

    @Column(name = "delete_on")
    private LocalDateTime deleteOn;

}