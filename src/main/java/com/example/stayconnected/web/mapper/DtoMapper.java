package com.example.stayconnected.web.mapper;


import com.example.stayconnected.email.model.Email;
import com.example.stayconnected.notification_preference.model.NotificationPreference;
import com.example.stayconnected.web.dto.EmailResponse;
import com.example.stayconnected.web.dto.NotificationPreferenceResponse;
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


    public static NotificationPreferenceResponse fromPreference(NotificationPreference preference) {

        return NotificationPreferenceResponse.builder()
                .notificationsEnabled(preference.isNotificationsEnabled())
                .bookingConfirmationEnabled(preference.isBookingConfirmationEnabled())
                .bookingCancellationEnabled(preference.isBookingCancellationEnabled())
                .passwordChangeEnabled(preference.isPasswordChangeEnabled())
                .build();
    }
}
