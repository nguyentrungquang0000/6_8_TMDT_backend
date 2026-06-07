package com.quangnt.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Integer id;
    private String method;
    private BigDecimal amount;
    private String transactionId;
    private String status;
    private Integer bookingId;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
    private String paymentUrl;
}