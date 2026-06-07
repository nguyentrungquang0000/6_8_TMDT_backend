package com.quangnt.ecom.controller;

import com.quangnt.common.builder.ResponseBuilder;
import com.quangnt.common.dto.ResponseDto;
import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.ecom.dto.ShowtimeCreateRequest;
import com.quangnt.ecom.dto.ShowtimeResponse;
import com.quangnt.ecom.dto.ShowtimeSearch;
import com.quangnt.ecom.service.ShowtimeService;
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
@RequestMapping("/v1/showtimes")
@RequiredArgsConstructor
public class ShowtimeController {
    private final ShowtimeService showtimeService;

    @PostMapping
    public ResponseEntity<ResponseDto<ShowtimeResponse>> create(@RequestBody @Valid ShowtimeCreateRequest request) {
        ShowtimeResponse response = showtimeService.create(request);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<ShowtimeResponse>> update(@PathVariable Integer id, @RequestBody ShowtimeCreateRequest request) {
        ShowtimeResponse response = showtimeService.update(id, request);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Object>> delete(@PathVariable Integer id) {
        showtimeService.delete(id);
        return ResponseBuilder.success(null, ResponseCode.SUCCESS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<ShowtimeResponse>> getOne(@PathVariable Integer id) {
        ShowtimeResponse response = showtimeService.getOne(id);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<ShowtimeResponse>>> search(@ModelAttribute ShowtimeSearch request) {
        var showTimeResponses = showtimeService.search(request);
        return ResponseEntity.ok(showTimeResponses);
    }
}
