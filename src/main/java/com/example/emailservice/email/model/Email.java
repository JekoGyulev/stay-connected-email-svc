package com.example.emailservice.email.model;

import com.example.emailservice.email.enums.EmailStatus;
import com.example.emailservice.email.enums.EmailTrigger;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "emails")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column
    private String subject;
    @Column(columnDefinition = "TEXT")
    private String body;
    @Column(name = "email_trigger",nullable = false)
    @Enumerated(EnumType.STRING)
    private EmailTrigger emailTrigger;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmailStatus status;
    @Column(nullable = false)
    private UUID userId;
    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;
}
