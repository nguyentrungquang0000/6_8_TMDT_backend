package com.quangnt.ecom.entity;

import com.quangnt.common.base.BaseEntity;
import com.quangnt.ecom.dto.RoomType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
public class Room extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer cinemaId;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType type;

    private Integer totalRow;
    private Integer totalSeatOfRow;

}
