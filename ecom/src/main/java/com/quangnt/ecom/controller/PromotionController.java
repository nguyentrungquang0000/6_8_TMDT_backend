package com.quangnt.ecom.controller;

import com.quangnt.common.builder.ResponseBuilder;
import com.quangnt.common.dto.ResponseDto;
import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.ecom.dto.PromotionCreateRequest;
import com.quangnt.ecom.dto.PromotionResponse;
import com.quangnt.ecom.dto.PromotionUpdateRequest;
import com.quangnt.ecom.service.PromotionService;
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
@RequestMapping("/v1/promotions")
@RequiredArgsConstructor
public class PromotionController {
    private final PromotionService promotionService;

    @PostMapping
    public ResponseEntity<ResponseDto<PromotionResponse>> create(@RequestBody PromotionCreateRequest request) {
        PromotionResponse response = promotionService.create(request);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<PromotionResponse>> update(@PathVariable Integer id, @RequestBody PromotionUpdateRequest request) {
        PromotionResponse response = promotionService.update(id, request);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @DeleteMapping
    public ResponseEntity<ResponseDto<Object>> delete(@RequestBody List<Integer> ids) {
        promotionService.delete(ids);
        return ResponseBuilder.success(null, ResponseCode.SUCCESS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<PromotionResponse>> getOne(@PathVariable Integer id) {
        PromotionResponse response = promotionService.getOne(id);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<Page<PromotionResponse>>> search(Pageable pageable) {
        Page<PromotionResponse> response = promotionService.search(pageable);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }
}
