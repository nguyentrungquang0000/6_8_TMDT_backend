package com.quangnt.ecom.entity;

import com.quangnt.ecom.dto.SeatType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "showtime_id", nullable = false)
    private Integer showtimeId;

    @Column(name = "seat_id", nullable = false)
    private Integer seatId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatType type;

    private BigDecimal price;

    @Column(name = "status", nullable = false)
    private boolean isSold = false;
}
