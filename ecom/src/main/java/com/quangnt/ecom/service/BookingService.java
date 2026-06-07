package com.quangnt.ecom.service;

import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.common.exception.BusinessException;
import com.quangnt.ecom.dto.BookingCreateRequest;
import com.quangnt.ecom.dto.BookingResponse;
import com.quangnt.ecom.entity.Booking;
import com.quangnt.ecom.entity.BookingDetail;
import com.quangnt.ecom.entity.User;
import com.quangnt.ecom.repository.BookingDetailRepository;
import com.quangnt.ecom.repository.BookingRepository;
import com.quangnt.ecom.repository.SeatRepository;
import com.quangnt.ecom.repository.TicketRepository;
import com.quangnt.ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final BookingDetailRepository bookingDetailRepository;
    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;

    public BookingResponse create(BookingCreateRequest request) {
        String id = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        var seats = seatRepository.findAllById(request.getSeatIds());
        if (CollectionUtils.isEmpty(seats)) {
            throw new BusinessException(ResponseCode.NOT_FOUND);
        }
        var tickets = ticketRepository.findAllBySeatIdIn(request.getSeatIds());
        Booking booking = Booking.builder()
                .user(user)
                .status(request.getStatus())
                .build();
        bookingRepository.save(booking);
        List<BookingDetail> bookingDetails = tickets.stream()
                .map(ticket -> BookingDetail.builder()
                        .ticketId(ticket.getId())
                        .price(ticket.getPrice())
                        .bookingId(booking.getId())
                        .build())
                .toList();
        bookingDetailRepository.saveAll(bookingDetails);
        return mapToResponse(booking);
    }

    public void delete(List<Integer> ids) {
        bookingRepository.deleteAllById(ids);
    }

    public BookingResponse getOne(Integer id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        return mapToResponse(booking);
    }

    public Page<BookingResponse> search(Pageable pageable) {
        return bookingRepository.findAll(pageable).map(this::mapToResponse);
    }

    private BookingResponse mapToResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .userId(booking.getUser().getId())
                .status(booking.getStatus())
                .createdAt(booking.getCreatedAt())
                .createdBy(booking.getCreatedBy())
                .updatedAt(booking.getUpdatedAt())
                .updatedBy(booking.getUpdatedBy())
                .build();
    }
}
