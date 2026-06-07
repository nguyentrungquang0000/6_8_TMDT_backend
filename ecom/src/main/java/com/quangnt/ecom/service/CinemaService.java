package com.quangnt.ecom.service;

import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.common.exception.BusinessException;
import com.quangnt.ecom.dto.CinemaRequest;
import com.quangnt.ecom.dto.CinemaResponse;
import com.quangnt.ecom.entity.Cinema;
import com.quangnt.ecom.mapper.CinemaMapper;
import com.quangnt.ecom.repository.CinemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CinemaService {
    private final CinemaRepository cinemaRepository;
    private final CinemaMapper cinemaMapper;

    public CinemaResponse create(CinemaRequest request) {
        Cinema cinema = cinemaMapper.toEntity(request);
        Cinema saved = cinemaRepository.save(cinema);
        return cinemaMapper.toResponse(saved);
    }

    public CinemaResponse update(Integer id, CinemaRequest request) {
        Cinema cinema = cinemaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        cinemaMapper.update(cinema, request);
        Cinema saved = cinemaRepository.save(cinema);
        return cinemaMapper.toResponse(saved);
    }

    public void delete(Integer id) {
        Cinema cinema = cinemaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        cinema = cinemaMapper.delete(cinema);
        cinemaRepository.save(cinema);
    }

    public CinemaResponse getOne(Integer id) {
        Cinema cinema = cinemaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        return cinemaMapper.toResponse(cinema);
    }

    public List<CinemaResponse> search() {
        List<Cinema> cinemas = cinemaRepository.findAllByDeletedIsFalse();
        return cinemaMapper.toResponses(cinemas);
    }

    public Map<Integer, Cinema> getCinemaMap(Set<Integer> cinemaIds) {
        if (cinemaIds == null || cinemaIds.isEmpty()) {
            return Collections.emptyMap();
        }

        return cinemaRepository.findByIdIn(cinemaIds).stream()
            .collect(Collectors.toMap(
                    Cinema::getId,
                    cinema -> cinema
            ));
    }

    public Cinema getCinemaById(Integer cinemaId) {
        return cinemaRepository.findById(cinemaId).orElse(null);
    }

}
