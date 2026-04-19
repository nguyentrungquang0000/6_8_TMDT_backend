package com.quangnt.ecom.controller;

import com.quangnt.common.builder.ResponseBuilder;
import com.quangnt.common.dto.ResponseDto;
import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.ecom.dto.BookingCreateRequest;
import com.quangnt.ecom.dto.BookingResponse;
import com.quangnt.ecom.dto.BookingUpdateRequest;
import com.quangnt.ecom.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<ResponseDto<BookingResponse>> create(@RequestBody BookingCreateRequest request) {
        BookingResponse response = bookingService.create(request);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<BookingResponse>> update(@PathVariable Integer id, @RequestBody BookingUpdateRequest request) {
        BookingResponse response = bookingService.update(id, request);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @DeleteMapping
    public ResponseEntity<ResponseDto<Object>> delete(@RequestBody List<Integer> ids) {
        bookingService.delete(ids);
        return ResponseBuilder.success(null, ResponseCode.SUCCESS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<BookingResponse>> getOne(@PathVariable Integer id) {
        BookingResponse response = bookingService.getOne(id);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<Page<BookingResponse>>> search(Pageable pageable) {
        Page<BookingResponse> response = bookingService.search(pageable);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }
}
