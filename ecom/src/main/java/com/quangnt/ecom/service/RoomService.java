package com.quangnt.ecom.service;

import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.common.exception.BusinessException;
import com.quangnt.ecom.dto.RoomCreateRequest;
import com.quangnt.ecom.dto.RoomResponse;
import com.quangnt.ecom.dto.RoomUpdateRequest;
import com.quangnt.ecom.entity.Cinema;
import com.quangnt.ecom.entity.Room;
import com.quangnt.ecom.repository.CinemaRepository;
import com.quangnt.ecom.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final CinemaRepository cinemaRepository;

    public RoomResponse create(RoomCreateRequest request) {
        Cinema cinema = cinemaRepository.findById(request.getCinemaId())
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        Room room = Room.builder()
                .name(request.getName())
                .totalSeats(request.getTotalSeats())
                .cinema(cinema)
                .type(request.getType())
                .build();
        Room saved = roomRepository.save(room);
        return mapToResponse(saved);
    }

    public RoomResponse update(Integer id, RoomUpdateRequest request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        Cinema cinema = cinemaRepository.findById(request.getCinemaId())
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        room.setName(request.getName());
        room.setTotalSeats(request.getTotalSeats());
        room.setCinema(cinema);
        room.setType(request.getType());
        Room saved = roomRepository.save(room);
        return mapToResponse(saved);
    }

    public void delete(List<Integer> ids) {
        roomRepository.deleteAllById(ids);
    }

    public RoomResponse getOne(Integer id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        return mapToResponse(room);
    }

    public Page<RoomResponse> search(Pageable pageable) {
        return roomRepository.findAll(pageable).map(this::mapToResponse);
    }

    private RoomResponse mapToResponse(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .name(room.getName())
                .totalSeats(room.getTotalSeats())
                .cinemaId(room.getCinema().getId())
                .type(room.getType())
                .createdAt(room.getCreatedAt())
                .createdBy(room.getCreatedBy())
                .updatedAt(room.getUpdatedAt())
                .updatedBy(room.getUpdatedBy())
                .build();
    }
}
