package com.example.emailservice.email.service.impl;

import com.example.emailservice.email.enums.EmailTrigger;
import com.example.emailservice.email.model.Email;
import com.example.emailservice.email.repository.EmailRepository;
import com.example.emailservice.email.service.EmailService;
import com.example.emailservice.web.dto.UserRegisteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

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
            log.info("Email has been sent to " + event.getUserEmail());
        } catch (MailException ex) {
            log.error("Email failed due to : {}", ex.getMessage());
        }
    }
}
