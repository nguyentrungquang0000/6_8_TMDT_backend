package com.quangnt.ecom.service;

import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.common.exception.BusinessException;
import com.quangnt.ecom.dto.BookingCreateRequest;
import com.quangnt.ecom.dto.BookingResponse;
import com.quangnt.ecom.dto.BookingUpdateRequest;
import com.quangnt.ecom.entity.Booking;
import com.quangnt.ecom.entity.Promotion;
import com.quangnt.ecom.entity.Showtime;
import com.quangnt.ecom.entity.User;
import com.quangnt.ecom.repository.BookingRepository;
import com.quangnt.ecom.repository.PromotionRepository;
import com.quangnt.ecom.repository.ShowtimeRepository;
import com.quangnt.ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ShowtimeRepository showtimeRepository;
    private final PromotionRepository promotionRepository;

    public BookingResponse create(BookingCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        Showtime showtime = showtimeRepository.findById(request.getShowtimeId())
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        Promotion promotion = null;
        if (request.getPromotionId() != null) {
            promotion = promotionRepository.findById(request.getPromotionId())
                    .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        }
        Booking booking = Booking.builder()
                .user(user)
                .promotion(promotion)
                .totalAmount(request.getTotalAmount())
                .discountAmount(request.getDiscountAmount())
                .finalAmount(request.getFinalAmount())
                .status(request.getStatus())
                .build();
        Booking saved = bookingRepository.save(booking);
        return mapToResponse(saved);
    }

    public BookingResponse update(Integer id, BookingUpdateRequest request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        Showtime showtime = showtimeRepository.findById(request.getShowtimeId())
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        Promotion promotion = null;
        if (request.getPromotionId() != null) {
            promotion = promotionRepository.findById(request.getPromotionId())
                    .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        }
        booking.setUser(user);
        booking.setPromotion(promotion);
        booking.setTotalAmount(request.getTotalAmount());
        booking.setDiscountAmount(request.getDiscountAmount());
        booking.setFinalAmount(request.getFinalAmount());
        booking.setStatus(request.getStatus());
        Booking saved = bookingRepository.save(booking);
        return mapToResponse(saved);
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
                .promotionId(booking.getPromotion() != null ? booking.getPromotion().getId() : null)
                .totalAmount(booking.getTotalAmount())
                .discountAmount(booking.getDiscountAmount())
                .finalAmount(booking.getFinalAmount())
                .status(booking.getStatus())
                .createdAt(booking.getCreatedAt())
                .createdBy(booking.getCreatedBy())
                .updatedAt(booking.getUpdatedAt())
                .updatedBy(booking.getUpdatedBy())
                .build();
    }
}
