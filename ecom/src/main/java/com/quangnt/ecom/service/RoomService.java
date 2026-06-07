package com.quangnt.ecom.service;

import com.quangnt.common.builder.ResponseBuilder;
import com.quangnt.common.dto.MetaData;
import com.quangnt.common.dto.ResponseDto;
import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.common.exception.BusinessException;
import com.quangnt.ecom.dto.RoomRequest;
import com.quangnt.ecom.dto.RoomResponse;
import com.quangnt.ecom.dto.RoomSearch;
import com.quangnt.ecom.entity.Cinema;
import com.quangnt.ecom.entity.Room;
import com.quangnt.ecom.entity.Seat;
import com.quangnt.ecom.mapper.RoomMapper;
import com.quangnt.ecom.mapper.SeatMapper;
import com.quangnt.ecom.repository.CinemaRepository;
import com.quangnt.ecom.repository.RoomRepository;
import com.quangnt.ecom.repository.SeatRepository;
import com.quangnt.ecom.repository.ShowtimeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final CinemaRepository cinemaRepository;
    private final RoomMapper roomMapper;
    private final CinemaService cinemaService;
    private final SeatRepository seatRepository;
    private final SeatMapper seatMapper;
    private final ShowtimeRepository showtimeRepository;

    @Transactional
    public RoomResponse create(RoomRequest request) {
        Cinema cinema = cinemaRepository.findById(request.getCinemaId())
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        Room room = roomMapper.toEntity(request);
        Room saved = roomRepository.save(room);
        List<Seat> seatList = new ArrayList<>();
        for (int i = 1; i <= room.getTotalRow(); i++) {
            for (int j = 1; j <= room.getTotalSeatOfRow(); j++) {
                seatList.add(seatMapper.toEntity(saved.getId(), i, j));
            }
        }
        seatRepository.saveAll(seatList);
        return roomMapper.toResponse(saved, cinema.getName());
    }

    @Transactional
    public RoomResponse update(Integer id, RoomRequest request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        cinemaRepository.findById(request.getCinemaId())
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        room = roomMapper.update(room, request);
        if (!request.getTotalRow().equals(room.getTotalRow()) || !request.getTotalSeatOfRow().equals(room.getTotalSeatOfRow())) {
            if (showtimeRepository.existsByRoomId(room.getId())) {
                throw new BusinessException(ResponseCode.BAD_REQUEST, "Không thể sửa số lượng hàng ghế và số lượng ghế trong hàng khi có showtime");
            }
            List<Seat> seatList = new ArrayList<>();
            for (int i = 1; i <= room.getTotalRow(); i++) {
                for (int j = 1; j <= room.getTotalSeatOfRow(); j++) {
                    seatList.add(seatMapper.toEntity(room.getId(), i, j));
                }
            }
            seatRepository.deleteAllByRoomId(room.getId());
            seatRepository.saveAll(seatList);
        }
        Room saved = roomRepository.save(room);
        return roomMapper.toResponse(saved);
    }

    public void delete(Integer id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        room = roomMapper.delete(room);
        roomRepository.save(room);
    }

    public RoomResponse getOne(Integer id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        Cinema cinema = cinemaService.getCinemaById(room.getCinemaId());
        return roomMapper.toResponse(room, cinema.getName());
    }

    public ResponseEntity<ResponseDto<List<RoomResponse>>> search(RoomSearch request) {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        Pageable pageable = PageRequest.of(request.page(), request.size(), sort);
        Page<Room> rooms = roomRepository.search(request.cinemaId(), pageable);
        Set<Integer> cinemaIds = rooms.getContent().stream()
                .map(Room::getCinemaId)
                .collect(Collectors.toSet());
        Map<Integer, Cinema> cinemaMap = cinemaService.getCinemaMap(cinemaIds);
        List<RoomResponse> response = roomMapper.toResponses(rooms.getContent(), cinemaMap);
        MetaData metaData = MetaData.builder()
                .pageSize(request.size())
                .currentPage(request.page())
                .totalPage(rooms.getTotalPages())
                .build();
        return ResponseBuilder.success(response, ResponseCode.SUCCESS, metaData);

    }

}
