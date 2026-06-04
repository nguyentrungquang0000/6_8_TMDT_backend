package com.quangnt.ecom.entity;

import com.quangnt.common.enumeration.TicketStatus;
import com.quangnt.ecom.dto.SeatType;
import jakarta.persistence.*;
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

    @Column(name = "row_number", nullable = false)
    private Integer rowNumber;

    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

    @Column(name = "seat", nullable = false)
    private String seat;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatType type;

    @Column(name = "price", nullable = false)
    private BigDecimal price;
}
