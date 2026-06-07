package com.quangnt.ecom.repository;

import com.quangnt.ecom.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

}
