package com.quangnt.ecom.controller;

import com.quangnt.ecom.dto.TicketResponse;
import com.quangnt.ecom.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @GetMapping("/sold")
    public ResponseEntity<List<TicketResponse>> getTicketSold(@RequestParam Integer showtimeId) {
        List<TicketResponse> ticketResponseList = ticketService.getTicketHasSold(showtimeId);
        return new ResponseEntity<>(ticketResponseList, HttpStatus.OK);
    }
}
