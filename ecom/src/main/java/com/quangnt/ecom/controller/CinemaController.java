package com.quangnt.ecom.controller;

import com.quangnt.common.builder.ResponseBuilder;
import com.quangnt.common.dto.ResponseDto;
import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.ecom.dto.CinemaRequest;
import com.quangnt.ecom.dto.CinemaResponse;
import com.quangnt.ecom.dto.CinemaUpdateRequest;
import com.quangnt.ecom.service.CinemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/cinemas")
@RequiredArgsConstructor
public class CinemaController {
    private final CinemaService cinemaService;

    @PostMapping
    public ResponseEntity<ResponseDto<CinemaResponse>> create(@RequestBody CinemaRequest request) {
        CinemaResponse response = cinemaService.create(request);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<CinemaResponse>> update(@PathVariable Integer id, @RequestBody CinemaRequest request) {
        CinemaResponse response = cinemaService.update(id, request);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Object>> delete(@PathVariable Integer id) {
        cinemaService.delete(id);
        return ResponseBuilder.success(null, ResponseCode.SUCCESS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<CinemaResponse>> getOne(@PathVariable Integer id) {
        CinemaResponse response = cinemaService.getOne(id);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<CinemaResponse>>> search() {
        List<CinemaResponse> response = cinemaService.search();
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }
}
