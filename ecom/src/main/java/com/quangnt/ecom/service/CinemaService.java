package com.quangnt.ecom.service;

import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.common.exception.BusinessException;
import com.quangnt.ecom.dto.CinemaCreateRequest;
import com.quangnt.ecom.dto.CinemaResponse;
import com.quangnt.ecom.dto.CinemaUpdateRequest;
import com.quangnt.ecom.entity.Cinema;
import com.quangnt.ecom.repository.CinemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CinemaService {
    private final CinemaRepository cinemaRepository;

    public CinemaResponse create(CinemaCreateRequest request) {
        Cinema cinema = Cinema.builder()
                .name(request.getName())
                .address(request.getAddress())
                .city(request.getCity())
                .build();
        Cinema saved = cinemaRepository.save(cinema);
        return mapToResponse(saved);
    }

    public CinemaResponse update(Integer id, CinemaUpdateRequest request) {
        Cinema cinema = cinemaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        cinema.setName(request.getName());
        cinema.setAddress(request.getAddress());
        cinema.setCity(request.getCity());
        Cinema saved = cinemaRepository.save(cinema);
        return mapToResponse(saved);
    }

    public void delete(List<Integer> ids) {
        cinemaRepository.deleteAllById(ids);
    }

    public CinemaResponse getOne(Integer id) {
        Cinema cinema = cinemaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        return mapToResponse(cinema);
    }

    public Page<CinemaResponse> search(Pageable pageable) {
        return cinemaRepository.findAll(pageable).map(this::mapToResponse);
    }

    private CinemaResponse mapToResponse(Cinema cinema) {
        return CinemaResponse.builder()
                .id(cinema.getId())
                .name(cinema.getName())
                .address(cinema.getAddress())
                .city(cinema.getCity())
                .createdAt(cinema.getCreatedAt())
                .createdBy(cinema.getCreatedBy())
                .updatedAt(cinema.getUpdatedAt())
                .updatedBy(cinema.getUpdatedBy())
                .build();
    }
}
