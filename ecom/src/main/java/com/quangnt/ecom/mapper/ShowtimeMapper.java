package com.quangnt.ecom.mapper;

import com.quangnt.ecom.dto.ShowtimeCreateRequest;
import com.quangnt.ecom.dto.ShowtimeResponse;
import com.quangnt.ecom.entity.Movie;
import com.quangnt.ecom.entity.Room;
import com.quangnt.ecom.entity.Showtime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface ShowtimeMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "COMING_SOON")
    @Mapping(target = "endTime", expression = "java(request.getStartTime().plusMinutes(20).plusMinutes(movie.getDuration()))")
    Showtime toEntity(ShowtimeCreateRequest request, Movie movie);

    @Mapping(target = "movieName", expression = "java(movieName)")
    @Mapping(target = "roomName", expression = "java(roomName)")
    ShowtimeResponse toResponse(Showtime saved, String movieName, String roomName);

    @Mapping(target = "status", source = "request.status")
    @Mapping(target = "endTime", expression = "java(request.getStartTime().plusMinutes(20).plusMinutes(movie.getDuration()))")
    Showtime update(@MappingTarget Showtime showtime, ShowtimeCreateRequest request, Movie movie);

    default List<ShowtimeResponse> toResponses(List<Showtime> content, Map<Integer, Movie> movieMap, Map<Integer, Room> roomMap){
        return content.stream().map(showtime -> {
            return toResponse(showtime,
                    movieMap.get(showtime.getMovieId()) != null ? movieMap.get(showtime.getMovieId()).getTitle() : null,
                    roomMap.get(showtime.getRoomId()) != null ? roomMap.get(showtime.getRoomId()).getName() : null);
        }).toList();
    }
}
