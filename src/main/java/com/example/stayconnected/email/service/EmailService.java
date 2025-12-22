package com.example.stayconnected.email.service;


import com.example.stayconnected.email.enums.EmailStatus;
import com.example.stayconnected.email.model.Email;
import com.example.stayconnected.event.payload.PasswordChangedEvent;
import com.example.stayconnected.event.payload.ReservationBookedEvent;
import com.example.stayconnected.event.payload.ReservationCancelledEvent;
import com.example.stayconnected.event.payload.UserRegisteredEvent;
import org.springframework.data.domain.Page;


import java.util.UUID;

public interface EmailService {

    Page<Email> getAllEmailsByUserIdSortedByCreateDate(int pageNumber, int pageSize, String search, UUID userId);

    Page<Email> getAllEmailsByUserIdAndStatusSorted(int pageNumber, int pageSize, UUID userId, String status);

    void handleUserRegistered(UserRegisteredEvent event);

    void handleReservationBooked(ReservationBookedEvent event);

    void handleReservationCancelled(ReservationCancelledEvent event);

    void handlePasswordChanged(PasswordChangedEvent event);

    long getTotalEmailsByUserId(UUID userId, String emailStatus);
}
