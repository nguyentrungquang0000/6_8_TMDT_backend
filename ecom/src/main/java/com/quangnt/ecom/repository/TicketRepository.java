package com.quangnt.ecom.repository;

import com.quangnt.ecom.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    List<Ticket> findAllByShowtimeIdAndIsSold(Integer showtimeId, boolean isSold);

    List<Ticket> findAllBySeatIdIn(List<Integer> seatIds);
}
