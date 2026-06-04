package com.quangnt.ecom.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequest {
    @NotNull
    private String name;
    @NotNull
    private Integer cinemaId;
    @NotNull
    private RoomType type;
    @NotNull
    private Integer totalRow;
    @NotNull
    private Integer totalSeatOfRow;
}