package com.example.stayconnected.notification_preference.repository;


import com.example.stayconnected.notification_preference.model.NotificationPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreference, UUID> {
}
