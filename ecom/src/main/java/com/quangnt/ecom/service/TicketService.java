package com.quangnt.ecom.service;

import com.quangnt.ecom.dto.TicketResponse;
import com.quangnt.ecom.entity.Ticket;
import com.quangnt.ecom.mapper.TicketMapper;
import com.quangnt.ecom.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    public List<TicketResponse> getTicketHasSold(Integer showtimeId) {
        List<Ticket> tickets = ticketRepository.findAllByShowtimeIdAndIsSold(showtimeId, true);
        return tickets.stream()
                .map(ticketMapper::toResponse)
                .collect(Collectors.toList());
    }
}
