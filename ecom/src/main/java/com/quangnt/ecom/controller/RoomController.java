package com.quangnt.ecom.controller;

import com.quangnt.common.builder.ResponseBuilder;
import com.quangnt.common.dto.ResponseDto;
import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.ecom.dto.RoomRequest;
import com.quangnt.ecom.dto.RoomResponse;
import com.quangnt.ecom.dto.RoomSearch;
import com.quangnt.ecom.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<ResponseDto<RoomResponse>> create(@Valid @RequestBody RoomRequest request) {
        RoomResponse response = roomService.create(request);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<RoomResponse>> update(@Valid @PathVariable Integer id, @RequestBody RoomRequest request) {
        RoomResponse response = roomService.update(id, request);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Object>> delete(@PathVariable Integer id) {
        roomService.delete(id);
        return ResponseBuilder.success(null, ResponseCode.SUCCESS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<RoomResponse>> getOne(@PathVariable Integer id) {
        RoomResponse response = roomService.getOne(id);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<RoomResponse>>> search(@ModelAttribute RoomSearch request) {
        return roomService.search(request);
    }
}
