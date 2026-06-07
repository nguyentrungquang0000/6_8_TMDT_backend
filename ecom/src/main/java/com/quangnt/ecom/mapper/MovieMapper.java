package com.quangnt.ecom.mapper;

import com.quangnt.ecom.dto.MovieRequest;
import com.quangnt.ecom.dto.MovieResponse;
import com.quangnt.ecom.entity.Movie;
import org.mapstruct.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    @Mapping(target = "status", expression = "java(request.getStatus())")
    @Mapping(target = "deleted", expression = "java(false)")
    Movie toEntity(MovieRequest request);

    @Mapping(target = "posterUrl", expression = "java(getUrl(movie.getPosterId(), urlMap))")
    @Mapping(target = "teaserUrl", expression = "java(getUrl(movie.getTeaserId(), urlMap))")
    MovieResponse toResponse(Movie movie, Map<String, String> urlMap);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "isTrending", expression = "java(request.getIsTrending())")
    void update(@MappingTarget Movie movie, MovieRequest request);

    default String getUrl(String mediaId, Map<String, String> urlMap){
        return urlMap.get(mediaId);
    }

    default List<MovieResponse> toResponses(List<Movie> movies, Map<String, String> urlMap){
        return movies.stream().map(movie -> toResponse(movie, urlMap)).collect(Collectors.toList());
    }

    @Mapping(target = "deleted", expression = "java(true)")
    Movie delete(Movie movie);

    default List<Movie> removeTrendings(List<Movie> movies){
        return movies.stream().peek(movie -> {
            movie.setIsTrending(false);
            movie.setTrendingOrder(null);
        }).collect(Collectors.toList());
    }
}
