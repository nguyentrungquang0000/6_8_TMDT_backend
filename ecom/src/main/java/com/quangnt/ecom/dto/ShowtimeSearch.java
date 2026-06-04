package com.quangnt.ecom.dto;

import java.time.LocalDate;

public record ShowtimeSearch(
        Integer page,
        Integer size,
        Integer cinemaId,
        String movieName,
        LocalDate date,
        ShowtimeStatus status
) {
}
