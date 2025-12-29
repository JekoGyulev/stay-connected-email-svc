package com.example.stayconnected.web.dto;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertNotificationPreferenceRequest {

    private UUID userId;
    private boolean notificationsEnabled;
    private boolean bookingConfirmationEnabled;
    private boolean bookingCancellationEnabled;
    private boolean passwordChangeEnabled;

}
