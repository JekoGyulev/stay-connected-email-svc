package com.example.stayconnected.event.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationBookedEvent {

    private UUID userId;
    private String userEmail;
    private LocalDate reservationStartDate;
    private LocalDate reservationEndDate;
    private BigDecimal reservationTotalPrice;

}
