package com.quangnt.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowtimeResponse {
    private Integer id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer movieId;
    private Integer roomId;
    private BigDecimal basePrice;
    private Integer availableSeats;
    private ShowtimeStatus status;
    private Instant createdAt;
    private UUID createdBy;
    private Instant updatedAt;
    private UUID updatedBy;
}