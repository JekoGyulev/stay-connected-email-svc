package com.example.emailservice.event;

import com.example.emailservice.email.service.EmailService;
import com.example.emailservice.event.payload.UserRegisteredEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private final EmailService emailService;

    @Autowired
    public KafkaConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "user-registered-event.v1", groupId = "email-svc")
    public void consumeUserRegistered(UserRegisteredEvent event) {
        emailService.handleUserRegistered(event);
    }

}
