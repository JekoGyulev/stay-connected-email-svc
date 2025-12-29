package com.example.stayconnected.web.controller;

import com.example.stayconnected.notification_preference.model.NotificationPreference;
import com.example.stayconnected.notification_preference.service.NotificationPreferenceService;
import com.example.stayconnected.web.dto.NotificationPreferenceResponse;
import com.example.stayconnected.web.dto.UpsertNotificationPreferenceRequest;
import com.example.stayconnected.web.mapper.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notification-preferences")
public class NotificationPreferenceController {

    private final NotificationPreferenceService notificationPreferenceService;

    @Autowired
    public NotificationPreferenceController(NotificationPreferenceService notificationPreferenceService) {
        this.notificationPreferenceService = notificationPreferenceService;
    }

    @GetMapping
    public ResponseEntity<NotificationPreferenceResponse> getNotificationPreference(@RequestParam(value = "userId") UUID userId) {

        NotificationPreference preference = this.notificationPreferenceService.getNotificationPreferenceByUserId(userId);

        NotificationPreferenceResponse response = DtoMapper.fromPreference(preference);

        return ResponseEntity.ok(response);
    }


    @PostMapping
    public ResponseEntity<Void> upsertNotificationPreference(@RequestBody UpsertNotificationPreferenceRequest request){

        boolean created = this.notificationPreferenceService.upsert(request);

        return created
                ? ResponseEntity.status(HttpStatus.CREATED).build()
                : ResponseEntity.status(HttpStatus.OK).build();
    }



}
