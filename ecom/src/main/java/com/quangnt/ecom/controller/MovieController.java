package com.quangnt.ecom.controller;

import com.quangnt.common.builder.ResponseBuilder;
import com.quangnt.common.dto.ResponseDto;
import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.ecom.dto.MovieListRequest;
import com.quangnt.ecom.dto.MovieRequest;
import com.quangnt.ecom.dto.MovieResponse;
import com.quangnt.ecom.dto.MovieSearchRequest;
import com.quangnt.ecom.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @PostMapping
    public ResponseEntity<ResponseDto<MovieResponse>> create(@RequestBody MovieRequest request) {
        MovieResponse response = movieService.create(request);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @GetMapping("/trending")
    public ResponseEntity<ResponseDto<List<MovieResponse>>> getTrending() {
        List<MovieResponse> response = movieService.getTrending();
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @PutMapping("/reorder")
    public ResponseEntity<ResponseDto<Void>> reorder(@RequestBody MovieListRequest request) {
        movieService.reorder(request);
        return ResponseBuilder.success(null, ResponseCode.SUCCESS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<MovieResponse>> update(@PathVariable Integer id, @RequestBody MovieRequest request) {
        MovieResponse response = movieService.update(id, request);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @PutMapping("/remove-trending")
    public ResponseEntity<ResponseDto<Void>> removeTrending(@RequestBody MovieListRequest request) {
         movieService.removeTrending(request);
        return ResponseBuilder.success(null, ResponseCode.SUCCESS);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Object>> delete(@PathVariable Integer id) {
        movieService.delete(id);
        return ResponseBuilder.success(null, ResponseCode.SUCCESS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<MovieResponse>> getOne(@PathVariable Integer id) {
        MovieResponse response = movieService.getOne(id);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<MovieResponse>>> search(@ModelAttribute MovieSearchRequest request) {
        return movieService.search(request);
    }

}
