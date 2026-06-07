package com.quangnt.ecom.controller;

import com.quangnt.common.builder.ResponseBuilder;
import com.quangnt.common.dto.ResponseDto;
import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.ecom.dto.SeatCreateRequest;
import com.quangnt.ecom.dto.SeatResponse;
import com.quangnt.ecom.service.SeatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/seats")
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;

    @PutMapping
    public ResponseEntity<ResponseDto<List<SeatResponse>>> createOrUpdate(@RequestBody @Valid List<SeatCreateRequest> request) {
        List<SeatResponse> response = seatService.create(request);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }


    @GetMapping("/{roomId}")
    public ResponseEntity<ResponseDto<List<SeatResponse>>> search(@PathVariable Integer roomId) {
        List<SeatResponse> response = seatService.search(roomId);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }
}
