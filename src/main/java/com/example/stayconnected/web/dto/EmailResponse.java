package com.example.stayconnected.web.dto;

import com.example.stayconnected.email.enums.EmailStatus;
import com.example.stayconnected.email.enums.EmailTrigger;
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
    private EmailStatus emailStatus;
    private LocalDateTime createdAt;
    private UUID userId;

}
