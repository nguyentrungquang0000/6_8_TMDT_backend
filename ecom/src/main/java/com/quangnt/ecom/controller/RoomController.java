package com.quangnt.ecom.controller;

import com.quangnt.common.builder.ResponseBuilder;
import com.quangnt.common.dto.ResponseDto;
import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.ecom.dto.RoomCreateRequest;
import com.quangnt.ecom.dto.RoomResponse;
import com.quangnt.ecom.dto.RoomUpdateRequest;
import com.quangnt.ecom.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<ResponseDto<RoomResponse>> create(@RequestBody RoomCreateRequest request) {
        RoomResponse response = roomService.create(request);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<RoomResponse>> update(@PathVariable Integer id, @RequestBody RoomUpdateRequest request) {
        RoomResponse response = roomService.update(id, request);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @DeleteMapping
    public ResponseEntity<ResponseDto<Object>> delete(@RequestBody List<Integer> ids) {
        roomService.delete(ids);
        return ResponseBuilder.success(null, ResponseCode.SUCCESS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<RoomResponse>> getOne(@PathVariable Integer id) {
        RoomResponse response = roomService.getOne(id);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<Page<RoomResponse>>> search(Pageable pageable) {
        Page<RoomResponse> response = roomService.search(pageable);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }
}
