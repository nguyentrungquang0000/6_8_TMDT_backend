package com.quangnt.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private Integer id;
    private String type;
    private String subject;
    private String body;
    private String status;
    private String userId;
    private Integer bookingId;
    private LocalDateTime sentAt;
    private LocalDateTime createdAt;
}