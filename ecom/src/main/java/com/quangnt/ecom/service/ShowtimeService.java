package com.quangnt.ecom.service;

import com.quangnt.common.dto.MetaData;
import com.quangnt.common.dto.ResponseDto;
import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.common.exception.BusinessException;
import com.quangnt.ecom.dto.ShowtimeCreateRequest;
import com.quangnt.ecom.dto.ShowtimeResponse;
import com.quangnt.ecom.dto.ShowtimeSearch;
import com.quangnt.ecom.entity.Movie;
import com.quangnt.ecom.entity.Room;
import com.quangnt.ecom.entity.Seat;
import com.quangnt.ecom.entity.Showtime;
import com.quangnt.ecom.entity.Ticket;
import com.quangnt.ecom.mapper.ShowtimeMapper;
import com.quangnt.ecom.mapper.TicketMapper;
import com.quangnt.ecom.repository.MovieRepository;
import com.quangnt.ecom.repository.RoomRepository;
import com.quangnt.ecom.repository.SeatRepository;
import com.quangnt.ecom.repository.ShowtimeRepository;
import com.quangnt.ecom.repository.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowtimeService {
    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final ShowtimeMapper showtimeMapper;
    private final SeatRepository seatRepository;
    private final TicketMapper ticketMapper;
    private final TicketRepository ticketRepository;

    @Transactional
    public ShowtimeResponse create(ShowtimeCreateRequest request) {
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND, "Movie", request.getMovieId()));
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND, "Room", request.getRoomId()));
        if (showtimeRepository.checkTime(request.getStartTime(), request.getStartTime().plusMinutes(20).plusMinutes(movie.getDuration()), room.getId())) {
            throw new BusinessException(ResponseCode.BAD_REQUEST, "Showtime overlaps with existing showtime in the same room");
        }
        Showtime entity = showtimeMapper.toEntity(request, movie);
        Showtime saved = showtimeRepository.save(entity);
        List<Seat> seats = seatRepository.findAllByRoomId(room.getId());
        if (CollectionUtils.isEmpty(seats)) {
            throw new BusinessException(ResponseCode.BAD_REQUEST, "No seats found for the room");
        }
        List<Ticket> ticketList = ticketMapper.toEntities(seats, saved.getId());
        ticketRepository.saveAll(ticketList);
        return showtimeMapper.toResponse(saved, movie.getTitle(), room.getName());
    }

    public ShowtimeResponse update(Integer id, ShowtimeCreateRequest request) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        if (showtimeRepository.checkTime(request.getStartTime(), request.getStartTime().plusMinutes(20).plusMinutes(movie.getDuration()), room.getId())) {
            throw new BusinessException(ResponseCode.BAD_REQUEST, "Showtime overlaps with existing showtime in the same room");
        }
        showtime = showtimeMapper.update(showtime, request, movie);
        showtime = showtimeRepository.save(showtime);
        return showtimeMapper.toResponse(showtime, movie.getTitle(), room.getName());
    }

    public void delete(Integer id) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND, "Showtime", id));
        showtimeRepository.delete(showtime);
    }

    public ShowtimeResponse getOne(Integer id) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        Movie movie = movieRepository.findById(showtime.getMovieId()).orElse(null);
        Room room = roomRepository.findById(showtime.getRoomId()).orElse(null);
        return showtimeMapper.toResponse(showtime, movie != null ? movie.getTitle() : null, room != null ? room.getName() : null);
    }

    public ResponseDto<List<ShowtimeResponse>> search(ShowtimeSearch request) {
        Sort sort = Sort.by(Sort.Direction.DESC, "startTime");
        int pageNumber = request.page() == null ? 0 : request.page();
        int pageSize = request.size() == null ? 10 : request.size();
        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize,
                sort);
        Page<Showtime> showTimes = showtimeRepository.search(
                request.movieName(),
                request.cinemaId(),
                request.status(),
                request.date(),
                pageable);
        Set<Integer> movieIds = showTimes.getContent().stream()
                .map(Showtime::getMovieId)
                .collect(Collectors.toSet());
        List<Movie> movies = movieRepository.findAllByIdIn(movieIds);
        Map<Integer, Movie> movieMap = movies.stream()
                .collect(Collectors.toMap(Movie::getId, Function.identity()));
        List<Integer> roomIds = showTimes.getContent()
                .stream()
                .map(Showtime::getRoomId)
                .toList();
        List<Room> rooms = roomRepository.findAllByIdIn(roomIds);
        Map<Integer, Room> roomMap = rooms.stream()
                .collect(Collectors.toMap(Room::getId, Function.identity()));
        List<ShowtimeResponse> response = showtimeMapper.toResponses(showTimes.getContent(), movieMap, roomMap);
        MetaData metadata = MetaData.builder()
                .totalPage(showTimes.getTotalPages())
                .currentPage(pageNumber)
                .pageSize(pageSize)
                .build();
        return new ResponseDto<>(response, metadata);
    }

}
