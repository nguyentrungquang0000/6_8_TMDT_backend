package com.quangnt.ecom.entity;

import com.quangnt.common.base.BaseEntity;
import com.quangnt.ecom.dto.SeatType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "seats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Seat extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "room_id", nullable = false)
    private Integer roomId;

    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

    @Column(name = "row_number", nullable = false)
    private Integer rowNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatType type = SeatType.STANDARD;

    @Column(name = "base_price", nullable = false)
    private Double priceMultiplier;
}
