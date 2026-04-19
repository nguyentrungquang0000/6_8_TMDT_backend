package com.quangnt.ecom.controller;

import com.quangnt.common.builder.ResponseBuilder;
import com.quangnt.common.dto.ResponseDto;
import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.ecom.dto.PaymentCreateRequest;
import com.quangnt.ecom.dto.PaymentResponse;
import com.quangnt.ecom.dto.PaymentUpdateRequest;
import com.quangnt.ecom.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ResponseDto<PaymentResponse>> create(@RequestBody PaymentCreateRequest request) {
        PaymentResponse response = paymentService.create(request);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<PaymentResponse>> update(@PathVariable Integer id, @RequestBody PaymentUpdateRequest request) {
        PaymentResponse response = paymentService.update(id, request);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @DeleteMapping
    public ResponseEntity<ResponseDto<Object>> delete(@RequestBody List<Integer> ids) {
        paymentService.delete(ids);
        return ResponseBuilder.success(null, ResponseCode.SUCCESS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<PaymentResponse>> getOne(@PathVariable Integer id) {
        PaymentResponse response = paymentService.getOne(id);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<Page<PaymentResponse>>> search(Pageable pageable) {
        Page<PaymentResponse> response = paymentService.search(pageable);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }
}
