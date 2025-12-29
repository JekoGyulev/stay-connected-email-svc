package com.example.stayconnected.notification_preference.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notification_preferences")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "user_id",nullable = false)
    private UUID userId;
    @Column(name = "are_notifications_enabled", nullable = false)
    private boolean areNotificationsEnabled;
    @Column(name = "is_booking_confirmation_notification_enabled", nullable = false)
    private boolean bookingConfirmationEnabled;
    @Column(name = "is_booking_cancellation_notification_enabled", nullable = false)
    private boolean bookingCancellationEnabled;
    @Column(name = "is_password_change_notification_enabled", nullable = false)
    private boolean passwordChangeEnabled;
    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(name = "updated_at",  nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;



}
