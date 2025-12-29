package com.example.stayconnected.notification_preference.service;

import com.example.stayconnected.notification_preference.model.NotificationPreference;
import com.example.stayconnected.web.dto.UpsertNotificationPreferenceRequest;

import java.util.UUID;

public interface NotificationPreferenceService {

    NotificationPreference getNotificationPreferenceByUserId(UUID userId);

    boolean upsert(UpsertNotificationPreferenceRequest request);

}
