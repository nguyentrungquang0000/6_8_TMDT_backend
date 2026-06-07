package com.quangnt.ecom.service;

import com.quangnt.common.builder.ResponseBuilder;
import com.quangnt.common.dto.MetaData;
import com.quangnt.common.dto.ResponseDto;
import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.common.exception.BusinessException;
import com.quangnt.ecom.dto.*;
import com.quangnt.ecom.entity.Media;
import com.quangnt.ecom.entity.Movie;
import com.quangnt.ecom.mapper.MovieMapper;
import com.quangnt.ecom.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final MediaService mediaService;

    @Transactional
    public MovieResponse create(MovieRequest request) {
        Media poster = mediaService.getMediaById(request.getPosterId());
        Media teaser = mediaService.getMediaById(request.getTeaserId());
        Movie movie = movieMapper.toEntity(request);
        Movie saved = movieRepository.save(movie);
        poster.setStatus(true);
        teaser.setStatus(true);
        List<String> mediaIds = List.of(poster.getId(), teaser.getId());
        Map<String, String> urlMap = mediaService.getPreviewUrls(mediaIds);
        return movieMapper.toResponse(saved, urlMap);
    }

    public MovieResponse update(Integer id, MovieRequest request) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        if (request.getPosterId() != null && !request.getPosterId().equals(movie.getPosterId())){
            Media poster = mediaService.getMediaById(request.getPosterId());
            poster.setStatus(true);
            mediaService.deleteMediaById(movie.getPosterId());
        }
        if (request.getTeaserId() != null && !request.getTeaserId().equals(movie.getTeaserId())){
            Media teaser = mediaService.getMediaById(request.getTeaserId());
            teaser.setStatus(true);
            mediaService.deleteMediaById(teaser.getId());
        }
        movieMapper.update(movie, request);
        Movie saved = movieRepository.save(movie);
        List<String> mediaIds = List.of(movie.getPosterId(), movie.getTeaserId());
        Map<String, String> urlMap = mediaService.getPreviewUrls(mediaIds);
        return movieMapper.toResponse(saved, urlMap);
    }

    public void delete(Integer id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        movie = movieMapper.delete(movie);
        movieRepository.save(movie);
        mediaService.deletes(List.of(movie.getPosterId(), movie.getTeaserId()));
    }

    public MovieResponse getOne(Integer id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        List<String> mediaIds = List.of(movie.getPosterId(), movie.getTeaserId());
        Map<String, String> urlMap = mediaService.getPreviewUrls(mediaIds);
        return movieMapper.toResponse(movie, urlMap);
    }

    public ResponseEntity<ResponseDto<List<MovieResponse>>> search(MovieSearchRequest request) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(request.page(), request.size(), sort);
        Page<Movie> movies = movieRepository.search(request.keyword(), request.status(), pageable);
        List<String> mediaIds = new ArrayList<>();
        for (Movie movie : movies.getContent()) {
            mediaIds.add(movie.getPosterId());
            mediaIds.add(movie.getTeaserId());
        }
        Map<String, String> urlMap = mediaService.getPreviewUrls(mediaIds);
        List<MovieResponse> response = movieMapper.toResponses(movies.getContent(), urlMap);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS, MetaData.builder()
                .currentPage(request.page())
                .pageSize(request.size())
                .totalPage(movies.getTotalPages())
            .build());
    }

    public List<MovieResponse> getTrending() {
        List<Movie> movies = movieRepository.findAllByIsTrendingAndDeletedFalse();
        List<String> mediaIds = new ArrayList<>();
        for (Movie movie : movies) {
            mediaIds.add(movie.getPosterId());
            mediaIds.add(movie.getTeaserId());
        }
        Map<String, String> urlMap = mediaService.getPreviewUrls(mediaIds);
        return movieMapper.toResponses(movies, urlMap);
    }

    public void reorder(MovieListRequest request) {
        List<Movie> movies = movieRepository.findAllByIdIn(request.movieIds());

        Map<Integer, Movie> movieMap = movies.stream()
                .collect(Collectors.toMap(Movie::getId, Function.identity()));

        int index = 1;
        for (Integer movieId : request.movieIds()) {
            Movie movie = movieMap.get(movieId);
            if (movie != null) {
                movie.setTrendingOrder(index++);
            }
        }

        movieRepository.saveAll(movies);
    }

    public void removeTrending(MovieListRequest request) {
        List<Movie> movies = movieRepository.findAllByIdIn(request.movieIds());
        if (movies.isEmpty()) {
            throw new BusinessException(ResponseCode.NOT_FOUND);
        }
        movies = movieMapper.removeTrendings(movies);
        movieRepository.saveAll(movies);
    }
}
