package com.quangnt.ecom.dto;

import jakarta.validation.constraints.NotNull;
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
    private Integer id;
    @NotNull
    private String rowNumber;
    @NotNull
    private String seatNumber;
    @NotNull
    private Integer roomId;
    @NotNull
    private SeatType type;
    @NotNull
    private Double priceMultiplier;
}