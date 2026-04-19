package com.quangnt.ecom.service;

import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.common.exception.BusinessException;
import com.quangnt.ecom.dto.SeatCreateRequest;
import com.quangnt.ecom.dto.SeatResponse;
import com.quangnt.ecom.dto.SeatUpdateRequest;
import com.quangnt.ecom.entity.Room;
import com.quangnt.ecom.entity.Seat;
import com.quangnt.ecom.repository.RoomRepository;
import com.quangnt.ecom.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;
    private final RoomRepository roomRepository;

    public SeatResponse create(SeatCreateRequest request) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        Seat seat = Seat.builder()
                .seatNumber(request.getSeatNumber())
                .rowLabel(request.getRowLabel())
                .room(room)
                .type(request.getType())
                .basePrice(request.getBasePrice())
                .build();
        Seat saved = seatRepository.save(seat);
        return mapToResponse(saved);
    }

    public SeatResponse update(Integer id, SeatUpdateRequest request) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        seat.setSeatNumber(request.getSeatNumber());
        seat.setRowLabel(request.getRowLabel());
        seat.setRoom(room);
        seat.setType(request.getType());
        seat.setBasePrice(request.getBasePrice());
        Seat saved = seatRepository.save(seat);
        return mapToResponse(saved);
    }

    public void delete(List<Integer> ids) {
        seatRepository.deleteAllById(ids);
    }

    public SeatResponse getOne(Integer id) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        return mapToResponse(seat);
    }

    public Page<SeatResponse> search(Pageable pageable) {
        return seatRepository.findAll(pageable).map(this::mapToResponse);
    }

    private SeatResponse mapToResponse(Seat seat) {
        return SeatResponse.builder()
                .id(seat.getId())
                .seatNumber(seat.getSeatNumber())
                .rowLabel(seat.getRowLabel())
                .roomId(seat.getRoom().getId())
                .type(seat.getType())
                .basePrice(seat.getBasePrice())
                .createdAt(seat.getCreatedAt())
                .createdBy(seat.getCreatedBy())
                .updatedAt(seat.getUpdatedAt())
                .updatedBy(seat.getUpdatedBy())
                .build();
    }
}
