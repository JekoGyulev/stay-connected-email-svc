package com.example.stayconnected.email.service;

import com.example.stayconnected.email.enums.EmailStatus;
import com.example.stayconnected.email.model.Email;
import com.example.stayconnected.event.payload.ReservationBookedEvent;
import com.example.stayconnected.event.payload.ReservationCancelledEvent;
import com.example.stayconnected.event.payload.UserRegisteredEvent;

import java.util.List;
import java.util.UUID;

public interface EmailService {

    void handleUserRegistered(UserRegisteredEvent event);

    void handleReservationBooked(ReservationBookedEvent event);

    void handleReservationCancelled(ReservationCancelledEvent event);

    List<Email> getAllEmailsByUserIdSortedByCreateDate(UUID userId);
}
