package com.quangnt.ecom.controller;

import com.quangnt.common.builder.ResponseBuilder;
import com.quangnt.common.dto.ResponseDto;
import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.ecom.dto.NotificationCreateRequest;
import com.quangnt.ecom.dto.NotificationResponse;
import com.quangnt.ecom.dto.NotificationUpdateRequest;
import com.quangnt.ecom.service.NotificationService;
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
@RequestMapping("/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<ResponseDto<NotificationResponse>> create(@RequestBody NotificationCreateRequest request) {
        NotificationResponse response = notificationService.create(request);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<NotificationResponse>> update(@PathVariable Integer id, @RequestBody NotificationUpdateRequest request) {
        NotificationResponse response = notificationService.update(id, request);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @DeleteMapping
    public ResponseEntity<ResponseDto<Object>> delete(@RequestBody List<Integer> ids) {
        notificationService.delete(ids);
        return ResponseBuilder.success(null, ResponseCode.SUCCESS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<NotificationResponse>> getOne(@PathVariable Integer id) {
        NotificationResponse response = notificationService.getOne(id);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<Page<NotificationResponse>>> search(Pageable pageable) {
        Page<NotificationResponse> response = notificationService.search(pageable);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }
}
