package com.quangnt.ecom.controller;

import com.quangnt.common.builder.ResponseBuilder;
import com.quangnt.common.dto.ResponseDto;
import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.ecom.dto.CinemaCreateRequest;
import com.quangnt.ecom.dto.CinemaResponse;
import com.quangnt.ecom.dto.CinemaUpdateRequest;
import com.quangnt.ecom.service.CinemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<ResponseDto<CinemaResponse>> create(@RequestBody CinemaCreateRequest request) {
        CinemaResponse response = cinemaService.create(request);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<CinemaResponse>> update(@PathVariable Integer id, @RequestBody CinemaUpdateRequest request) {
        CinemaResponse response = cinemaService.update(id, request);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @DeleteMapping
    public ResponseEntity<ResponseDto<Object>> delete(@RequestBody List<Integer> ids) {
        cinemaService.delete(ids);
        return ResponseBuilder.success(null, ResponseCode.SUCCESS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<CinemaResponse>> getOne(@PathVariable Integer id) {
        CinemaResponse response = cinemaService.getOne(id);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<Page<CinemaResponse>>> search(Pageable pageable) {
        Page<CinemaResponse> response = cinemaService.search(pageable);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }
}
