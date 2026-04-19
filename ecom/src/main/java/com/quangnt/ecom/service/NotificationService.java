package com.quangnt.ecom.service;

import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.common.exception.BusinessException;
import com.quangnt.ecom.dto.NotificationCreateRequest;
import com.quangnt.ecom.dto.NotificationResponse;
import com.quangnt.ecom.dto.NotificationUpdateRequest;
import com.quangnt.ecom.entity.Booking;
import com.quangnt.ecom.entity.Notification;
import com.quangnt.ecom.entity.User;
import com.quangnt.ecom.repository.BookingRepository;
import com.quangnt.ecom.repository.NotificationRepository;
import com.quangnt.ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    public NotificationResponse create(NotificationCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        Booking booking = null;
        if (request.getBookingId() != null) {
            booking = bookingRepository.findById(request.getBookingId())
                    .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        }
        Notification notification = Notification.builder()
                .type(Notification.NotificationType.valueOf(request.getType()))
                .subject(request.getSubject())
                .body(request.getBody())
                .status(Notification.NotificationStatus.valueOf(request.getStatus()))
                .user(user)
                .booking(booking)
                .sentAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        Notification saved = notificationRepository.save(notification);
        return mapToResponse(saved);
    }

    public NotificationResponse update(Integer id, NotificationUpdateRequest request) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        Booking booking = null;
        if (request.getBookingId() != null) {
            booking = bookingRepository.findById(request.getBookingId())
                    .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        }
        notification.setType(Notification.NotificationType.valueOf(request.getType()));
        notification.setSubject(request.getSubject());
        notification.setBody(request.getBody());
        notification.setStatus(Notification.NotificationStatus.valueOf(request.getStatus()));
        notification.setUser(user);
        notification.setBooking(booking);
        Notification saved = notificationRepository.save(notification);
        return mapToResponse(saved);
    }

    public void delete(List<Integer> ids) {
        notificationRepository.deleteAllById(ids);
    }

    public NotificationResponse getOne(Integer id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        return mapToResponse(notification);
    }

    public Page<NotificationResponse> search(Pageable pageable) {
        return notificationRepository.findAll(pageable).map(this::mapToResponse);
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .type(String.valueOf(notification.getType()))
                .subject(notification.getSubject())
                .body(notification.getBody())
                .status(String.valueOf(notification.getStatus()))
                .userId(notification.getUser().getId())
                .bookingId(notification.getBooking() != null ? notification.getBooking().getId() : null)
                .sentAt(notification.getSentAt())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
