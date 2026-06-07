package com.quangnt.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreateRequest {
    private String userId;
    private Integer showtimeId;
    private BookingStatus status;
    List<Integer> seatIds;
    private String qrCode;
}