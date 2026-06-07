package com.quangnt.ecom.service;

import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.common.exception.BusinessException;
import com.quangnt.ecom.dto.PaymentCreateRequest;
import com.quangnt.ecom.dto.PaymentMethod;
import com.quangnt.ecom.dto.PaymentResponse;
import com.quangnt.ecom.dto.PaymentStatus;
import com.quangnt.ecom.dto.PaymentUpdateRequest;
import com.quangnt.ecom.entity.Booking;
import com.quangnt.ecom.entity.BookingDetail;
import com.quangnt.ecom.entity.Payment;
import com.quangnt.ecom.repository.BookingDetailRepository;
import com.quangnt.ecom.repository.BookingRepository;
import com.quangnt.ecom.repository.PaymentRepository;
import com.quangnt.ecom.service.vnPay.VnPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final BookingDetailRepository bookingDetailRepository;
    private final VnPayService vnPayService;

    public PaymentResponse create(PaymentCreateRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        List<BookingDetail> bookingDetails = bookingDetailRepository.findAllByBookingId(booking.getId());
        var amount = bookingDetails.stream()
                .map(BookingDetail::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Payment payment = Payment.builder()
                .method(PaymentMethod.valueOf(request.getMethod()))
                .transactionId(request.getTransactionId())
                .status(PaymentStatus.valueOf(request.getStatus()))
                .amount(amount)
                .booking(booking)
                .paidAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        String paymentUrl = vnPayService.getUrlPayment(amount.longValue());
        Payment saved = paymentRepository.save(payment);
        return mapToResponse(saved, paymentUrl);
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
        return mapToResponse(saved, null);
    }

    public void delete(List<Integer> ids) {
        paymentRepository.deleteAllById(ids);
    }

    public PaymentResponse getOne(Integer id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        return mapToResponse(payment, null);
    }

    public Page<PaymentResponse> search(Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Payment> payments = paymentRepository.findAll(pageable);
        payments.map(payment -> mapToResponse(payment, null));
        return paymentRepository.findAll(pageable).map(payment -> mapToResponse(payment, null));
    }

    private PaymentResponse mapToResponse(Payment payment, String paymentUrl) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .method(String.valueOf(payment.getMethod()))
                .amount(payment.getAmount())
                .transactionId(payment.getTransactionId())
                .status(String.valueOf(payment.getStatus()))
                .bookingId(payment.getBooking().getId())
                .paidAt(payment.getPaidAt())
                .createdAt(payment.getCreatedAt())
                .paymentUrl(paymentUrl)
                .build();
    }
}
