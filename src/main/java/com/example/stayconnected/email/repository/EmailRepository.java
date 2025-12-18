package com.example.stayconnected.email.repository;

import com.example.stayconnected.email.enums.EmailStatus;
import com.example.stayconnected.email.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmailRepository extends JpaRepository<Email, UUID> {
    List<Email> findAllByUserIdOrderByCreatedAtDesc(UUID userId);

    List<Email> findAllByStatusAndUserId(EmailStatus status, UUID userId);

    List<Email> findBySubjectContainingIgnoreCaseAndUserIdOrderByCreatedAtDesc(String subject, UUID userId);
}
