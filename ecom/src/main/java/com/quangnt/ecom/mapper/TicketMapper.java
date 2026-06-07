package com.quangnt.ecom.mapper;

import com.quangnt.ecom.entity.Seat;
import com.quangnt.ecom.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    default List<Ticket> toEntities(List<Seat> seats, Integer showtimeId){
        return seats.stream().map(seat -> toEntity(seat, showtimeId)).toList();
    }

    @Mapping(target = "showtimeId", source = "showtimeId")
    @Mapping(target = "seatId", expression = ("java(String.valueOf(seat.getId()))"))
    @Mapping(target = "type", expression = ("java(seat.getType())"))
    Ticket toEntity(Seat seat,  Integer showtimeId);
}
