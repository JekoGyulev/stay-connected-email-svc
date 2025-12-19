package com.example.stayconnected.web.mapper;


import com.example.stayconnected.email.model.Email;
import com.example.stayconnected.web.dto.EmailResponse;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class DtoMapper {

    public static EmailResponse fromEmail(Email email) {
        return EmailResponse.builder()
                .emailId(email.getId())
                .subject(email.getSubject())
                .emailTrigger(email.getEmailTrigger())
                .emailStatus(email.getStatus())
                .userId(email.getUserId())
                .createdAt(email.getCreatedAt())
                .build();
    }




}
