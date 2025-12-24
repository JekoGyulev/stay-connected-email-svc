package com.example.stayconnected.event.payload;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostInquiryEvent {

    private UUID userId;
    private String userEmail;
    private String hosterEmail;
    private String subject;
    private String body;
    private String inquiryType;
    private String propertyTitle;



}
