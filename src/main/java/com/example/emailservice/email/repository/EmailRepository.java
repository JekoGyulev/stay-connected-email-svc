package com.example.emailservice.email.repository;

import com.example.emailservice.email.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmailRepository extends JpaRepository<Email, UUID> {
    List<Email> findAllByUserIdOrderByCreatedAtDesc(UUID userId);
}
