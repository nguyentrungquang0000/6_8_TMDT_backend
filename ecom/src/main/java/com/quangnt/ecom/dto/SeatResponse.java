package com.quangnt.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatResponse {
    private Integer id;

    private Integer seatNumber;
    private Integer rowNumber;

    private Integer roomId;
    private SeatType type;
    private BigDecimal priceMultiplier;

    private Instant createdAt;
    private UUID createdBy;
    private Instant updatedAt;
    private UUID updatedBy;
}