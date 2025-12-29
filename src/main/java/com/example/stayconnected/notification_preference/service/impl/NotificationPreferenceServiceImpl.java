package com.example.stayconnected.notification_preference.service.impl;


import com.example.stayconnected.notification_preference.model.NotificationPreference;
import com.example.stayconnected.notification_preference.repository.NotificationPreferenceRepository;
import com.example.stayconnected.notification_preference.service.NotificationPreferenceService;
import com.example.stayconnected.web.dto.UpsertNotificationPreferenceRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class NotificationPreferenceServiceImpl implements NotificationPreferenceService {


    private final NotificationPreferenceRepository notificationPreferenceRepository;

    @Autowired
    public NotificationPreferenceServiceImpl(NotificationPreferenceRepository notificationPreferenceRepository) {
        this.notificationPreferenceRepository = notificationPreferenceRepository;
    }


    @Override
    public NotificationPreference getNotificationPreferenceByUserId(UUID userId) {
        return this.notificationPreferenceRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Notification preference with user id [%s] not found"
                        .formatted(userId)));
    }

    @Override
    public boolean upsert(UpsertNotificationPreferenceRequest request) {

        Optional<NotificationPreference> optionalPreference = this.notificationPreferenceRepository.
                findByUserId(request.getUserId());


        if (optionalPreference.isPresent()) {

            NotificationPreference existingPreference = optionalPreference.get();
            existingPreference.setNotificationsEnabled(request.isNotificationsEnabled());
            existingPreference.setBookingConfirmationEnabled(request.isBookingConfirmationEnabled());
            existingPreference.setBookingCancellationEnabled(request.isBookingCancellationEnabled());
            existingPreference.setPasswordChangeEnabled(request.isPasswordChangeEnabled());

            this.notificationPreferenceRepository.save(existingPreference);

            log.info("Successfully updated notification preference for user with id [%s]"
                    .formatted(request.getUserId()));

            return false;
        }


        NotificationPreference preference =
                    NotificationPreference.builder()
                            .userId(request.getUserId())
                            .notificationsEnabled(request.isNotificationsEnabled())
                            .bookingCancellationEnabled(request.isBookingCancellationEnabled())
                            .bookingConfirmationEnabled(request.isBookingConfirmationEnabled())
                            .passwordChangeEnabled(request.isPasswordChangeEnabled())
                            .build();

        this.notificationPreferenceRepository.save(preference);


        log.info("Successfully created notification preference for user with id [%s]"
                    .formatted(request.getUserId()));

        return true;

    }
}
