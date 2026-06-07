package com.quangnt.ecom.controller;

import com.quangnt.common.builder.ResponseBuilder;
import com.quangnt.common.dto.ResponseDto;
import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.ecom.dto.BookingDetailCreateRequest;
import com.quangnt.ecom.dto.BookingDetailResponse;
import com.quangnt.ecom.dto.BookingDetailUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/booking-details")
@RequiredArgsConstructor
public class BookingDetailController {
    private final BookingDetailService bookingDetailService;

    @PostMapping
    public ResponseEntity<ResponseDto<BookingDetailResponse>> create(@RequestBody BookingDetailCreateRequest request) {
        BookingDetailResponse response = bookingDetailService.create(request);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<BookingDetailResponse>> update(@PathVariable Integer id, @RequestBody BookingDetailUpdateRequest request) {
        BookingDetailResponse response = bookingDetailService.update(id, request);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @DeleteMapping
    public ResponseEntity<ResponseDto<Object>> delete(@RequestBody List<Integer> ids) {
        bookingDetailService.delete(ids);
        return ResponseBuilder.success(null, ResponseCode.SUCCESS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<BookingDetailResponse>> getOne(@PathVariable Integer id) {
        BookingDetailResponse response = bookingDetailService.getOne(id);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<Page<BookingDetailResponse>>> search(Pageable pageable) {
        Page<BookingDetailResponse> response = bookingDetailService.search(pageable);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }
}
