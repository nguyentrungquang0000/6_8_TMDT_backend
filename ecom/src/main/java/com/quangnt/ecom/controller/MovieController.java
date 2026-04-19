package com.quangnt.ecom.controller;

import com.quangnt.common.builder.ResponseBuilder;
import com.quangnt.common.dto.ResponseDto;
import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.ecom.dto.MovieCreateRequest;
import com.quangnt.ecom.dto.MovieResponse;
import com.quangnt.ecom.dto.MovieUpdateRequest;
import com.quangnt.ecom.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<ResponseDto<MovieResponse>> create(@RequestBody MovieCreateRequest request) {
        MovieResponse response = movieService.create(request);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<MovieResponse>> update(@PathVariable Integer id, @RequestBody MovieUpdateRequest request) {
        MovieResponse response = movieService.update(id, request);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @DeleteMapping
    public ResponseEntity<ResponseDto<Object>> delete(@RequestBody List<Integer> ids) {
        movieService.delete(ids);
        return ResponseBuilder.success(null, ResponseCode.SUCCESS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<MovieResponse>> getOne(@PathVariable Integer id) {
        MovieResponse response = movieService.getOne(id);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<Page<MovieResponse>>> search(Pageable pageable) {
        Page<MovieResponse> response = movieService.search(pageable);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }
}
