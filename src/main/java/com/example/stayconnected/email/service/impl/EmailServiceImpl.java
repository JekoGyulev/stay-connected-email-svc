package com.example.stayconnected.email.service.impl;

import com.example.stayconnected.email.enums.EmailStatus;
import com.example.stayconnected.email.enums.EmailTrigger;
import com.example.stayconnected.email.model.Email;
import com.example.stayconnected.email.repository.EmailRepository;
import com.example.stayconnected.email.service.EmailService;
import com.example.stayconnected.event.payload.PasswordChangedEvent;
import com.example.stayconnected.event.payload.ReservationBookedEvent;
import com.example.stayconnected.event.payload.ReservationCancelledEvent;
import com.example.stayconnected.event.payload.UserRegisteredEvent;
import com.example.stayconnected.web.dto.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private static final String SUCCESSFUL_REGISTER_SUBJECT_MESSAGE = "Welcome to our platform!✈️";
    private static final String SUCCESSFUL_RESERVATION_BOOKED_SUBJECT_MESSAGE = "Your Reservation Has Been Successfully Booked";
    private static final String SUCCESSFUL_RESERVATION_CANCELLED_SUBJECT_MESSAGE = "Your Reservation Has Been Cancelled Successfully";
    private static final String SUCCESSFUL_PASSWORD_CHANGED_SUBJECT_MESSAGE = "Your Password Has Been Changed Successfully";



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

    private static final String SUCCESSFUL_RESERVATION_CANCELLED_BODY_MESSAGE = """
                                                    Hi %s,
                                                   \s
                                                    We wanted to let you know that your reservation from %s to %s has been successfully cancelled.
                                                   \s
                                                    A refund of €%.2f has been processed and should reflect in your account shortly.
                                                   \s
                                                    If this was a mistake or you’d like to make a new reservation, feel free to visit our platform and book again at your convenience.
                                                   \s
                                                    Thank you for using StayConnected!
                                                   \s
                                                    Best regards,
                                                    The StayConnected Team
           \s""";

    private static final String SUCCESSFUL_PASSWORD_CHANGED_BODY_MESSAGE = """
                                                    Hi %s,
                                                   \s
                                                    This is a confirmation that your account password was successfully changed at %s.
                                                   \s
                                                    If you made this change, no further action is required.
                                                   \s
                                                    If you did not change your password, please reset it immediately and contact our support team to secure your account.
                                                   \s
                                                    Thank you for using StayConnected!
                                                   \s
                                                    Best regards,
                                                    The StayConnected Team
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
    public void handleReservationCancelled(ReservationCancelledEvent event) {

        Email email = Email.builder()
                .subject(SUCCESSFUL_RESERVATION_CANCELLED_SUBJECT_MESSAGE)
                .body(SUCCESSFUL_RESERVATION_CANCELLED_BODY_MESSAGE
                        .formatted(event.getUsername(), event.getReservationStartDate(),
                                event.getReservationEndDate(), event.getReservationTotalPrice()))
                .emailTrigger(EmailTrigger.CANCEL_RESERVATION)
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
    public Page<Email> getAllEmailsByUserIdSortedByCreateDate(int pageNumber, int pageSize, String search, UUID userId) {

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        if (search != null) {
            return this.emailRepository
                    .findBySubjectContainingIgnoreCaseAndUserIdOrderByCreatedAtDesc(pageRequest, search,userId);
        }

        return this.emailRepository.findAllByUserIdOrderByCreatedAtDesc(pageRequest, userId);
    }

    @Override
    public Page<Email> getAllEmailsByUserIdAndStatusSorted(int pageNumber, int pageSize, UUID userId, String status) {

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        EmailStatus emailStatus = EmailStatus.valueOf(status);

        return this.emailRepository.findAllByUserIdAndStatusOrderByCreatedAtDesc(userId, emailStatus, pageRequest);
    }

    @Override
    public void handlePasswordChanged(PasswordChangedEvent event) {

        LocalDateTime changedAt = event.getChangedAt();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = changedAt.format(formatter);

        Email email = Email.builder()
                .subject(SUCCESSFUL_PASSWORD_CHANGED_SUBJECT_MESSAGE)
                .body(SUCCESSFUL_PASSWORD_CHANGED_BODY_MESSAGE.formatted(event.getUsername(), formattedDateTime))
                .emailTrigger(EmailTrigger.CHANGE_PASSWORD)
                .status(EmailStatus.PENDING)
                .userId(event.getUserId())
                .createdAt(changedAt)
                .build();


        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(email.getSubject());
        message.setText(email.getBody());
        message.setTo(event.getUserEmail());
        message.setFrom(fromEmailUsername);


        sendEmail(message, email, event.getUserEmail());
    }

    @Override
    public long getTotalEmailsByUserId(UUID userId, String emailStatus) {

        if (emailStatus == null || emailStatus.isEmpty()) {
            return this.emailRepository.countAllByUserId(userId);
        }

        EmailStatus status = EmailStatus.valueOf(emailStatus);

        return this.emailRepository.countAllByUserIdAndStatus(userId, status);
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
