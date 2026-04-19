package com.quangnt.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatCreateRequest {
    private String seatNumber;
    private String rowLabel;
    private Integer roomId;
    private SeatType type;
    private BigDecimal basePrice;
}