package com.quangnt.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {
    private Integer showtimeId;
    private Integer seatId;
    private SeatType type;
    private BigDecimal price;
    private boolean isSold;
}
