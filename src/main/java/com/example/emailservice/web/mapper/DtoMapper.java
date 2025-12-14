package com.example.emailservice.web.mapper;


import com.example.emailservice.email.model.Email;
import com.example.emailservice.web.dto.EmailResponse;
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
                .createdAt(LocalDateTime.now())
                .build();
    }




}
