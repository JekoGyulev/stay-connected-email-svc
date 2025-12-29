# StayConnected - Email Service

This repository contains the Email Service for the StayConnected platform. It is a Spring Boot microservice responsible for sending transactional emails in response to various user activities and system events. The service is designed to be event-driven, consuming messages from Apache Kafka to trigger email notifications.

## Features

*   **Event-Driven Architecture**: Listens to Kafka topics for events such as user registration, reservation booking/cancellation, password changes, and host inquiries.
*   **Transactional Emails**: Sends templated emails for different events to ensure clear communication with users.
*   **Email Persistence**: Stores a record of every email sent, including its subject, body, status (`PENDING`, `SENT`, `FAILED`), and the event that triggered it.
*   **REST API**: Provides HTTP endpoints to query the email history for a specific user, with filtering and pagination capabilities.
*   **Status Tracking**: Tracks the delivery status of each email, providing visibility into communication success and failures.
*   **API Documentation**: Integrated Swagger UI for easy exploration and testing of the API endpoints.

## How It Works

The service operates on an event-driven model:

1.  Other microservices within the StayConnected ecosystem publish events to designated Kafka topics when specific actions occur (e.g., a user registers, books a property, etc.).
2.  The `KafkaConsumer` component in this service is subscribed to these topics.
3.  Upon receiving an event, the consumer delegates the handling to the `EmailService`.
4.  The `EmailService` first creates and persists an `Email` entity to the database with a `PENDING` status. This creates a record of the intended communication.
5.  It then constructs and attempts to send the actual email using Spring Mail's `MailSender`.
6.  The status of the `Email` entity is updated to `SENT` on successful dispatch or `FAILED` if an exception occurs. This provides a persistent log and audit trail of all email communications.

## Technologies Used

*   **Backend**: Java 17, Spring Boot 3
*   **Data**: Spring Data JPA, MySQL, H2 (for tests)
*   **Messaging**: Spring for Apache Kafka
*   **Web**: Spring Web
*   **Email**: Spring Boot Starter Mail
*   **API Documentation**: Springdoc OpenAPI (Swagger UI)
*   **Build**: Apache Maven
*   **Utilities**: Lombok

## Kafka Event Consumption

The service consumes the following events from Kafka:

| Topic Name                       | Event Payload                  | Description                                            |
| -------------------------------- | ------------------------------ | ------------------------------------------------------ |
| `user-registered-event.v1`       | `UserRegisteredEvent`          | Triggers a welcome email upon new user registration.   |
| `reservation-booked-event.v1`    | `ReservationBookedEvent`       | Sends a booking confirmation email.                    |
| `reservation-cancelled-event.v1` | `ReservationCancelledEvent`    | Sends a reservation cancellation and refund email.     |
| `password-changed-event.v1`      | `PasswordChangedEvent`         | Notifies the user of a successful password change.     |
| `inquiry-host-event.v1`          | `HostInquiryEvent`             | Forwards a user's inquiry to the property host's email.|

## API Endpoints

The service exposes the following REST endpoints to manage and retrieve email data.

### Get Emails by User
- **Endpoint**: `GET /api/v1/emails`
- **Description**: Retrieves a paginated list of all emails sent to a specific user, sorted by creation date. It also supports searching by email subject.
- **Query Parameters**:
    - `pageNumber` (int, default: `0`): The page number to retrieve.
    - `pageSize` (int, default: `4`): The number of emails per page.
    - `userId` (UUID, required): The ID of the user.
    - `search` (String, optional): A string to search for in the email subject (case-insensitive).

### Get Emails by Status
- **Endpoint**: `GET /api/v1/emails/status`
- **Description**: Retrieves a paginated list of emails for a user, filtered by a specific status (`PENDING`, `SENT`, `FAILED`).
- **Query Parameters**:
    - `pageNumber` (int, default: `0`): The page number.
    - `pageSize` (int, default: `4`): The page size.
    - `userId` (UUID, required): The ID of the user.
    - `status` (String, required): The email status to filter by.

### Get Total Email Count
- **Endpoint**: `GET /api/v1/emails/total`
- **Description**: Returns the total count of emails for a user. Can be filtered by status.
- **Query Parameters**:
    - `userId` (UUID, required): The ID of the user.
    - `status` (String, optional): If provided, counts only emails with this status.

## Configuration

The application's configuration is located in `src/main/resources/application.properties`. The key properties to configure are:

*   **Database Connection**:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/email-service?createDatabaseIfNotExist=true
    spring.datasource.username=your_db_user
    spring.datasource.password=your_db_password
    ```

*   **Email Sender (SMTP)**:
    The service is configured to use Gmail's SMTP server. You must provide your credentials via environment variables.
    ```properties
    spring.mail.host=smtp.gmail.com
    spring.mail.port=587
    spring.mail.username=${SPRING_EMAIL_USERNAME}
    spring.mail.password=${SPRING_EMAIL_PASSWORD}
    spring.mail.properties.mail.smtp.auth=true
    spring.mail.properties.mail.smtp.starttls.enable=true
    ```

*   **Kafka Broker**:
    ```properties
    spring.kafka.bootstrap-servers=localhost:9092
    ```

## Getting Started

To run the Email Service locally, follow these steps:

### Prerequisites
*   Java 17 or higher
*   Maven
*   A running MySQL server
*   A running Apache Kafka instance

### Installation & Execution
1.  **Clone the repository**:
    ```sh
    git clone https://github.com/JekoGyulev/stay-connected-email-svc.git
    cd stay-connected-email-svc
    ```

2.  **Database Setup**:
    Create a MySQL database named `email-service`.

3.  **Configure Application**:
    - Open `src/main/resources/application.properties`.
    - Update the `spring.datasource.username` and `spring.datasource.password` properties with your MySQL credentials.
    - Ensure `spring.kafka.bootstrap-servers` points to your running Kafka broker.

4.  **Set Environment Variables**:
    Set the environment variables for the email account you want to use for sending emails.
    ```sh
    export SPRING_EMAIL_USERNAME="your-email@gmail.com"
    export SPRING_EMAIL_PASSWORD="your-gmail-app-password"
    ```

5.  **Run the application**:
    Use the Maven wrapper to build and run the service.
    ```sh
    ./mvnw spring-boot:run
    ```
    The service will start on `http://localhost:8082`.

## API Documentation (Swagger)

Once the application is running, you can access the interactive API documentation via Swagger UI at the following URL:
[http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)
