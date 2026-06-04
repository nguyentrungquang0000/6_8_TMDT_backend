package com.quangnt.ecom.service;

import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.common.exception.BusinessException;
import com.quangnt.ecom.dto.SeatCreateRequest;
import com.quangnt.ecom.dto.SeatResponse;
import com.quangnt.ecom.dto.SeatUpdateRequest;
import com.quangnt.ecom.entity.Room;
import com.quangnt.ecom.entity.Seat;
import com.quangnt.ecom.mapper.SeatMapper;
import com.quangnt.ecom.repository.RoomRepository;
import com.quangnt.ecom.repository.SeatRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;
    private final RoomRepository roomRepository;
    private final SeatMapper seatMapper;

    public List<SeatResponse> create(List<SeatCreateRequest> request) {
        if (request.isEmpty()) {
            throw new BusinessException(ResponseCode.BAD_REQUEST);
        }
        Set<Integer> ids = request.stream().map(SeatCreateRequest::getRoomId).collect(Collectors.toSet());
        if (ids.size() > 1) {
            throw new BusinessException(ResponseCode.BAD_REQUEST);
        }

        Integer roomId = ids.iterator().next();
        roomRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));

        List<Seat> seatOlds = seatRepository.findAllByRoomId(roomId);
        if (seatOlds.isEmpty()) {
            throw new BusinessException(ResponseCode.BAD_REQUEST);
        }

        Map<Integer, Seat> seatOldMap = seatOlds.stream().collect(Collectors.toMap(Seat::getId, s -> s));
        Map<Integer, SeatCreateRequest> requestMap = request.stream().collect(Collectors.toMap(SeatCreateRequest::getId, s -> s));
        List<Seat> entities = seatMapper.update(seatOldMap, requestMap);
        List<Seat> saved = seatRepository.saveAll(entities);
        return seatMapper.toResponses(saved);
    }


    public List<SeatResponse> search(Integer roomId) {
        List<Seat> seats = seatRepository.findAllByRoomId(roomId);
        return seatMapper.toResponses(seats);
    }
}
