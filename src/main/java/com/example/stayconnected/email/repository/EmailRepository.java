package com.example.stayconnected.email.repository;

import com.example.stayconnected.email.enums.EmailStatus;
import com.example.stayconnected.email.model.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.UUID;

@Repository
public interface EmailRepository extends JpaRepository<Email, UUID> {
    Page<Email> findAllByUserIdOrderByCreatedAtDesc(Pageable pageable, UUID userId);

    Page<Email> findAllByUserIdAndStatusOrderByCreatedAtDesc(UUID userId, EmailStatus status, Pageable pageable);

    Page<Email> findBySubjectContainingIgnoreCaseAndUserIdOrderByCreatedAtDesc(Pageable pageable, String subject, UUID userId);

    long countAllByUserId(UUID userId);

    long countAllByUserIdAndStatus(UUID userId, EmailStatus status);
}
