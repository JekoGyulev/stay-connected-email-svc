package com.example.stayconnected.web.controller;


import com.example.stayconnected.email.enums.EmailStatus;
import com.example.stayconnected.email.model.Email;
import com.example.stayconnected.email.service.EmailService;
import com.example.stayconnected.web.dto.EmailResponse;
import com.example.stayconnected.web.dto.PageResponse;
import com.example.stayconnected.web.mapper.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<PageResponse<EmailResponse>> getEmailsByUser(
                                                                @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                                @RequestParam(value = "pageSize", defaultValue = "4") int pageSize,
                                                                @RequestParam(value = "search", required = false) String search,
                                                                @RequestParam(value = "userId") UUID userId) {

        Page<Email> emailsByUser = this.emailService
                .getAllEmailsByUserIdSortedByCreateDate(pageNumber, pageSize,search,userId);

        List<EmailResponse> emailResponses = emailsByUser.getContent().stream()
                .map(DtoMapper::fromEmail)
                .toList();

        PageResponse<EmailResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(emailResponses);
        pageResponse.setTotalPages(emailsByUser.getTotalPages());
        pageResponse.setTotalElements(emailsByUser.getTotalElements());
        pageResponse.setCurrentPage(pageNumber);
        pageResponse.setPageSize(pageSize);


        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/total")
    public ResponseEntity<Long> getTotalEmailsByUserId(@RequestParam(value = "userId") UUID userId,
                                                       @RequestParam(value = "status", required = false) String emailStatus) {
        long totalCountEmails = this.emailService.getTotalEmailsByUserId(userId, emailStatus);

        return ResponseEntity.status(HttpStatus.OK).body(totalCountEmails);
    }

    @GetMapping("/status")
    public ResponseEntity<PageResponse<EmailResponse>> getEmailsByUserAndStatus(
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "4") int pageSize,
            @RequestParam(value = "userId") UUID userId,
            @RequestParam(value = "status") String status
    ) {

        Page<Email> emailsPage = this.emailService.getAllEmailsByUserIdAndStatusSorted(pageNumber, pageSize, userId, status);

        List<EmailResponse> emailResponses = emailsPage.getContent()
                .stream()
                .map(DtoMapper::fromEmail)
                .toList();


        PageResponse<EmailResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(emailResponses);
        pageResponse.setTotalPages(emailsPage.getTotalPages());
        pageResponse.setTotalElements(emailsPage.getTotalElements());
        pageResponse.setCurrentPage(pageNumber);
        pageResponse.setPageSize(pageSize);

        return ResponseEntity.ok(pageResponse);
    }


}
