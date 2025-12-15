package com.example.stayconnected.event;

import com.example.stayconnected.email.service.EmailService;
import com.example.stayconnected.event.payload.ReservationBookedEvent;
import com.example.stayconnected.event.payload.UserRegisteredEvent;
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
        this.emailService.handleUserRegistered(event);
    }

    @KafkaListener(topics = "reservation-booked-event.v1", groupId = "email-svc")
    public void consumeReservationBooked(ReservationBookedEvent event) {
        this.emailService.handleReservationBooked(event);
    }

    // Add kafka listeners for topic : reservation-cancelled-event.v1

}
