package com.example.emailservice.web.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class UserRegisteredEvent {

    private UUID userId;
    private String username;
    private String userEmail;
    private LocalDateTime createdAt;

}
