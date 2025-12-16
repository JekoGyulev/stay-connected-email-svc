package com.example.stayconnected.web.controller;


import com.example.stayconnected.email.enums.EmailStatus;
import com.example.stayconnected.email.model.Email;
import com.example.stayconnected.email.service.EmailService;
import com.example.stayconnected.web.dto.EmailResponse;
import com.example.stayconnected.web.mapper.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/emails")
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }


    @GetMapping
    public ResponseEntity<List<EmailResponse>> getEmailsByUser(@RequestParam(value = "userId") UUID userId) {
        List<Email> emailsByUser = this.emailService.getAllEmailsByUserIdSortedByCreateDate(userId);

        List<EmailResponse> emailResponses = emailsByUser.stream()
                .map(DtoMapper::fromEmail)
                .toList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(emailResponses);
    }


}
