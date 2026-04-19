package com.quangnt.ecom.service;

import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.common.exception.BusinessException;
import com.quangnt.ecom.dto.MovieCreateRequest;
import com.quangnt.ecom.dto.MovieResponse;
import com.quangnt.ecom.dto.MovieUpdateRequest;
import com.quangnt.ecom.entity.Media;
import com.quangnt.ecom.entity.Movie;
import com.quangnt.ecom.repository.MediaRepository;
import com.quangnt.ecom.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final MediaRepository mediaRepository;

    public MovieResponse create(MovieCreateRequest request) {
        Media poster = null;
        if (request.getPosterMediaId() != null) {
            poster = mediaRepository.findById(request.getPosterMediaId())
                    .orElseThrow(() -> new BusinessException(ResponseCode.MEDIA_NOTFOUND));
        }
        Movie movie = Movie.builder()
                .title(request.getTitle())
                .genre(request.getGenre())
                .duration(request.getDuration())
                .director(request.getDirector())
                .movieCast(request.getCast())
                .description(request.getDescription())
                .poster(poster)
                .releaseDate(request.getReleaseDate())
                .status(request.getStatus())
                .teaserUrl(request.getTeaserUrl())
                .reviewUrl(request.getReviewUrl())
                .build();
        Movie saved = movieRepository.save(movie);
        return mapToResponse(saved);
    }

    public MovieResponse update(Integer id, MovieUpdateRequest request) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        Media poster = null;
        if (request.getPosterMediaId() != null) {
            poster = mediaRepository.findById(request.getPosterMediaId())
                    .orElseThrow(() -> new BusinessException(ResponseCode.MEDIA_NOTFOUND));
        }
        movie.setTitle(request.getTitle());
        movie.setGenre(request.getGenre());
        movie.setDuration(request.getDuration());
        movie.setDirector(request.getDirector());
        movie.setDescription(request.getDescription());
        movie.setMovieCast(request.getCast());
        movie.setPoster(poster);
        movie.setReleaseDate(request.getReleaseDate());
        movie.setStatus(request.getStatus());
        movie.setTeaserUrl(request.getTeaserUrl());
        movie.setReviewUrl(request.getReviewUrl());
        Movie saved = movieRepository.save(movie);
        return mapToResponse(saved);
    }

    public void delete(List<Integer> ids) {
        movieRepository.deleteAllById(ids);
    }

    public MovieResponse getOne(Integer id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        return mapToResponse(movie);
    }

    public Page<MovieResponse> search(Pageable pageable) {
        return movieRepository.findAll(pageable).map(this::mapToResponse);
    }

    private MovieResponse mapToResponse(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .genre(movie.getGenre())
                .duration(movie.getDuration())
                .director(movie.getDirector())
                .cast(movie.getMovieCast())
                .description(movie.getDescription())
                .posterMediaId(movie.getPoster() != null ? movie.getPoster().getId() : null)
                .releaseDate(movie.getReleaseDate())
                .status(movie.getStatus())
                .teaserUrl(movie.getTeaserUrl())
                .reviewUrl(movie.getReviewUrl())
                .createdAt(movie.getCreatedAt())
                .createdBy(movie.getCreatedBy())
                .updatedAt(movie.getUpdatedAt())
                .updatedBy(movie.getUpdatedBy())
                .build();
    }
}
