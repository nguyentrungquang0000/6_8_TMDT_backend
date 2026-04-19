package com.quangnt.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationUpdateRequest {
    private String type;
    private String subject;
    private String body;
    private String status;
    private String userId;
    private Integer bookingId;
}