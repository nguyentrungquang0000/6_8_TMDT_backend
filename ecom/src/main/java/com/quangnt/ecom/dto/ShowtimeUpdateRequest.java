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
public class ShowtimeUpdateRequest {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer movieId;
    private Integer roomId;
    private BigDecimal basePrice;
    private Integer availableSeats;
    private ShowtimeStatus status;
}