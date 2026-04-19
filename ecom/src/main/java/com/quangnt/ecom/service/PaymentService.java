package com.quangnt.ecom.service;

import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.common.exception.BusinessException;
import com.quangnt.ecom.dto.*;
import com.quangnt.ecom.entity.Booking;
import com.quangnt.ecom.entity.Payment;
import com.quangnt.ecom.repository.BookingRepository;
import com.quangnt.ecom.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    public PaymentResponse create(PaymentCreateRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        Payment payment = Payment.builder()
                .method(PaymentMethod.valueOf(request.getMethod()))
                .amount(request.getAmount())
                .transactionId(request.getTransactionId())
                .status(PaymentStatus.valueOf(request.getStatus()))
                .booking(booking)
                .paidAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        Payment saved = paymentRepository.save(payment);
        return mapToResponse(saved);
    }

    public PaymentResponse update(Integer id, PaymentUpdateRequest request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        payment.setMethod(PaymentMethod.valueOf(request.getMethod()));
        payment.setAmount(request.getAmount());
        payment.setTransactionId(request.getTransactionId());
        payment.setStatus(PaymentStatus.valueOf(request.getStatus()));
        payment.setBooking(booking);
        Payment saved = paymentRepository.save(payment);
        return mapToResponse(saved);
    }

    public void delete(List<Integer> ids) {
        paymentRepository.deleteAllById(ids);
    }

    public PaymentResponse getOne(Integer id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        return mapToResponse(payment);
    }

    public Page<PaymentResponse> search(Pageable pageable) {
        return paymentRepository.findAll(pageable).map(this::mapToResponse);
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .method(String.valueOf(payment.getMethod()))
                .amount(payment.getAmount())
                .transactionId(payment.getTransactionId())
                .status(String.valueOf(payment.getStatus()))
                .bookingId(payment.getBooking().getId())
                .paidAt(payment.getPaidAt())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}
