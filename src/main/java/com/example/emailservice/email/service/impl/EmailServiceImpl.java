package com.example.emailservice.email.service.impl;

import com.example.emailservice.email.enums.EmailStatus;
import com.example.emailservice.email.enums.EmailTrigger;
import com.example.emailservice.email.model.Email;
import com.example.emailservice.email.repository.EmailRepository;
import com.example.emailservice.email.service.EmailService;
import com.example.emailservice.web.dto.UserRegisteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private static final String SUCCESSFUL_REGISTER_SUBJECT_MESSAGE = "Welcome to our platform!✈️";
    private static final String SUCCESSFUL_REGISTER_BODY_MESSAGE ="""
                                                    Hi %s,
                                                        \s
                                                    Thanks for signing up! We're excited to have you on board.
                                                    Explore the platform at your own pace and let us know if you ever need help.
                                                        \s
                                                    Enjoy your stay!
                                                 """;


    private final EmailRepository emailRepository;
    private final MailSender mailSender;

    @Autowired
    public EmailServiceImpl(EmailRepository emailRepository, MailSender mailSender) {
        this.emailRepository = emailRepository;
        this.mailSender = mailSender;
    }


    @Override
    public void handleUserRegistered(UserRegisteredEvent event) {
        Email email = Email.builder()
                .subject(SUCCESSFUL_REGISTER_SUBJECT_MESSAGE)
                .body(SUCCESSFUL_REGISTER_BODY_MESSAGE.formatted(event.getUsername()))
                .emailTrigger(EmailTrigger.REGISTER)
                .status(EmailStatus.PENDING)
                .userId(event.getUserId())
                .createdAt(event.getCreatedAt())
                .build();


        this.emailRepository.save(email);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(email.getSubject());
        message.setText(email.getBody());
        message.setTo(event.getUserEmail());

        try {
            this.mailSender.send(message);
            email.setStatus(EmailStatus.SENT);
            log.info("Email has been sent to " + event.getUserEmail());
        } catch (MailException ex) {
            log.error("Email failed due to : {}", ex.getMessage());
            email.setStatus(EmailStatus.FAILED);
        } finally {
            this.emailRepository.save(email);
        }
    }

    @Override
    public List<Email> getAllEmailsByUserIdSortedByCreateDate(UUID userId) {
        return this.emailRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
    }
}
