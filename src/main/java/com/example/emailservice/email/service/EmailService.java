package com.example.emailservice.email.service;

import com.example.emailservice.email.model.Email;
import com.example.emailservice.web.dto.UserRegisteredEvent;

import java.util.UUID;

public interface EmailService {

    void handleUserRegistered(UserRegisteredEvent event);
}
