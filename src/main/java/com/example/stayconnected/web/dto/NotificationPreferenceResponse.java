package com.example.stayconnected.web.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotificationPreferenceResponse {

    private boolean notificationsEnabled;
    private boolean bookingConfirmationEnabled;
    private boolean bookingCancellationEnabled;
    private boolean passwordChangeEnabled;

}
