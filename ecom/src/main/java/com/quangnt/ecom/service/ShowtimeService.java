package com.quangnt.ecom.service;

import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.common.exception.BusinessException;
import com.quangnt.ecom.dto.ShowtimeCreateRequest;
import com.quangnt.ecom.dto.ShowtimeResponse;
import com.quangnt.ecom.dto.ShowtimeUpdateRequest;
import com.quangnt.ecom.entity.Movie;
import com.quangnt.ecom.entity.Room;
import com.quangnt.ecom.entity.Showtime;
import com.quangnt.ecom.repository.MovieRepository;
import com.quangnt.ecom.repository.RoomRepository;
import com.quangnt.ecom.repository.ShowtimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowtimeService {
    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;

    public ShowtimeResponse create(ShowtimeCreateRequest request) {
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        Showtime showtime = Showtime.builder()
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .movie(movie)
                .room(room)
                .basePrice(request.getBasePrice())
                .availableSeats(request.getAvailableSeats())
                .status(request.getStatus())
                .build();
        Showtime saved = showtimeRepository.save(showtime);
        return mapToResponse(saved);
    }

    public ShowtimeResponse update(Integer id, ShowtimeUpdateRequest request) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        showtime.setStartTime(request.getStartTime());
        showtime.setEndTime(request.getEndTime());
        showtime.setMovie(movie);
        showtime.setRoom(room);
        showtime.setBasePrice(request.getBasePrice());
        showtime.setAvailableSeats(request.getAvailableSeats());
        showtime.setStatus(request.getStatus());
        Showtime saved = showtimeRepository.save(showtime);
        return mapToResponse(saved);
    }

    public void delete(List<Integer> ids) {
        showtimeRepository.deleteAllById(ids);
    }

    public ShowtimeResponse getOne(Integer id) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        return mapToResponse(showtime);
    }

    public Page<ShowtimeResponse> search(Pageable pageable) {
        return showtimeRepository.findAll(pageable).map(this::mapToResponse);
    }

    private ShowtimeResponse mapToResponse(Showtime showtime) {
        return ShowtimeResponse.builder()
                .id(showtime.getId())
                .startTime(showtime.getStartTime())
                .endTime(showtime.getEndTime())
                .movieId(showtime.getMovie().getId())
                .roomId(showtime.getRoom().getId())
                .basePrice(showtime.getBasePrice())
                .availableSeats(showtime.getAvailableSeats())
                .status(showtime.getStatus())
                .createdAt(showtime.getCreatedAt())
                .createdBy(showtime.getCreatedBy())
                .updatedAt(showtime.getUpdatedAt())
                .updatedBy(showtime.getUpdatedBy())
                .build();
    }
}
