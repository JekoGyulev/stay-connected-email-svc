package com.example.stayconnected.email.service.impl;

import com.example.stayconnected.email.enums.EmailStatus;
import com.example.stayconnected.email.enums.EmailTrigger;
import com.example.stayconnected.email.model.Email;
import com.example.stayconnected.email.repository.EmailRepository;
import com.example.stayconnected.email.service.EmailService;
import com.example.stayconnected.event.payload.ReservationBookedEvent;
import com.example.stayconnected.event.payload.UserRegisteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private static final String SUCCESSFUL_REGISTER_SUBJECT_MESSAGE = "Welcome to our platform!✈️";
    private static final String SUCCESSFUL_RESERVATION_BOOKED_SUBJECT_MESSAGE = "✅ Your Reservation Has Been Successfully Booked!";

    private static final String SUCCESSFUL_REGISTER_BODY_MESSAGE ="""
                                                    Hi %s,
                                                        \s
                                                    Thanks for signing up! We're excited to have you on board.
                                                    Explore the platform at your own pace and let us know if you ever need help.
                                                        \s
                                                    Enjoy your stay!
                                                 """;
    private static final String SUCCESSFUL_RESERVATION_BOOKED_BODY_MESSAGE = """
                                                    Hello,
                                           \s
                                                    We’re happy to inform you that your reservation has been successfully booked!
                                                    Your stay is scheduled for the date %s to %s, and everything is now confirmed.
                                                    The total price of your reservation is : €%.2f
                                                   \s
                                                    Our team is already preparing to ensure you have a smooth and enjoyable experience.
                                           \s
                                                    If you need to make any changes or have questions regarding your reservation,
                                                    feel free to contact our support team at any time. We look forward to welcoming
                                                    you and wish you a pleasant stay!
                                           \s
                                                    Best regards,
                                                    StayConnected Team
       \s""";


    private final EmailRepository emailRepository;
    private final MailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmailUsername;

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
                .createdAt(LocalDateTime.now())
                .build();


        this.emailRepository.save(email);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(email.getSubject());
        message.setText(email.getBody());
        message.setTo(event.getEmail());
        message.setFrom(fromEmailUsername);

        sendEmail(message, email, event.getEmail());
    }


    @Override
    public void handleReservationBooked(ReservationBookedEvent event) {

        Email email = Email.builder()
                .subject(SUCCESSFUL_RESERVATION_BOOKED_SUBJECT_MESSAGE)
                .body(SUCCESSFUL_RESERVATION_BOOKED_BODY_MESSAGE
                        .formatted(event.getReservationStartDate(), event.getReservationEndDate(), event.getReservationTotalPrice()))
                .emailTrigger(EmailTrigger.BOOK_RESERVATION)
                .status(EmailStatus.PENDING)
                .userId(event.getUserId())
                .createdAt(LocalDateTime.now())
                .build();



        SimpleMailMessage message =  new SimpleMailMessage();
        message.setSubject(email.getSubject());
        message.setText(email.getBody());
        message.setTo(event.getUserEmail());
        message.setFrom(fromEmailUsername);


        sendEmail(message, email, event.getUserEmail());
    }

    @Override
    public List<Email> getAllEmailsByUserIdSortedByCreateDate(UUID userId) {
        return this.emailRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
    }


    private void sendEmail(SimpleMailMessage message, Email email, String userEmail) {
        try {
            this.mailSender.send(message);
            email.setStatus(EmailStatus.SENT);
            log.info("Email has been sent to " + userEmail);
        } catch (MailException ex) {
            log.error("Email failed due to : {}", ex.getMessage());
            email.setStatus(EmailStatus.FAILED);
        } finally {
            this.emailRepository.save(email);
        }
    }

}
