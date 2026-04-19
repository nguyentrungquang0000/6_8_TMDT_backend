package com.quangnt.ecom.controller;

import com.quangnt.common.builder.ResponseBuilder;
import com.quangnt.common.dto.ResponseDto;
import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.ecom.dto.SeatCreateRequest;
import com.quangnt.ecom.dto.SeatResponse;
import com.quangnt.ecom.dto.SeatUpdateRequest;
import com.quangnt.ecom.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/seats")
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;

    @PostMapping
    public ResponseEntity<ResponseDto<SeatResponse>> create(@RequestBody SeatCreateRequest request) {
        SeatResponse response = seatService.create(request);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<SeatResponse>> update(@PathVariable Integer id, @RequestBody SeatUpdateRequest request) {
        SeatResponse response = seatService.update(id, request);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @DeleteMapping
    public ResponseEntity<ResponseDto<Object>> delete(@RequestBody List<Integer> ids) {
        seatService.delete(ids);
        return ResponseBuilder.success(null, ResponseCode.SUCCESS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<SeatResponse>> getOne(@PathVariable Integer id) {
        SeatResponse response = seatService.getOne(id);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<Page<SeatResponse>>> search(Pageable pageable) {
        Page<SeatResponse> response = seatService.search(pageable);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }
}
