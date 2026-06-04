package com.quangnt.ecom.dto;

public record RoomSearch (
        Integer page ,
        Integer size,
        Integer cinemaId
) {
}
