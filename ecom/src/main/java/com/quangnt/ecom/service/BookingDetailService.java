package com.quangnt.ecom.service;

import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.common.exception.BusinessException;
import com.quangnt.ecom.dto.BookingDetailCreateRequest;
import com.quangnt.ecom.dto.BookingDetailResponse;
import com.quangnt.ecom.dto.BookingDetailUpdateRequest;
import com.quangnt.ecom.entity.Booking;
import com.quangnt.ecom.entity.BookingDetail;
import com.quangnt.ecom.entity.Seat;
import com.quangnt.ecom.repository.BookingDetailRepository;
import com.quangnt.ecom.repository.BookingRepository;
import com.quangnt.ecom.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingDetailService {
    private final BookingDetailRepository bookingDetailRepository;
    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;

    public BookingDetailResponse create(BookingDetailCreateRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        Seat seat = seatRepository.findById(request.getSeatId())
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        BookingDetail bookingDetail = BookingDetail.builder()
                .booking(booking)
                .priceAtTime(request.getPriceAtTime())
                .build();
        BookingDetail saved = bookingDetailRepository.save(bookingDetail);
        return mapToResponse(saved);
    }

    public BookingDetailResponse update(Integer id, BookingDetailUpdateRequest request) {
        BookingDetail bookingDetail = bookingDetailRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        Seat seat = seatRepository.findById(request.getSeatId())
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        bookingDetail.setBooking(booking);
        bookingDetail.setPriceAtTime(request.getPriceAtTime());
        BookingDetail saved = bookingDetailRepository.save(bookingDetail);
        return mapToResponse(saved);
    }

    public void delete(List<Integer> ids) {
        bookingDetailRepository.deleteAllById(ids);
    }

    public BookingDetailResponse getOne(Integer id) {
        BookingDetail bookingDetail = bookingDetailRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        return mapToResponse(bookingDetail);
    }

    public Page<BookingDetailResponse> search(Pageable pageable) {
        return bookingDetailRepository.findAll(pageable).map(this::mapToResponse);
    }

    private BookingDetailResponse mapToResponse(BookingDetail bookingDetail) {
        return BookingDetailResponse.builder()
                .id(bookingDetail.getId())
                .bookingId(bookingDetail.getBooking().getId())
                .priceAtTime(bookingDetail.getPriceAtTime())
                .createdAt(bookingDetail.getCreatedAt())
                .createdBy(bookingDetail.getCreatedBy())
                .updatedAt(bookingDetail.getUpdatedAt())
                .updatedBy(bookingDetail.getUpdatedBy())
                .build();
    }
}
