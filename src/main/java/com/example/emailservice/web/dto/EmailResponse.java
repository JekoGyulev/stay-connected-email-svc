package com.example.emailservice.web.dto;

import com.example.emailservice.email.enums.EmailTrigger;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class EmailResponse {

    private UUID emailId;
    private String subject;
    private EmailTrigger emailTrigger;
    private LocalDateTime createdAt;
    private UUID userId;

}
