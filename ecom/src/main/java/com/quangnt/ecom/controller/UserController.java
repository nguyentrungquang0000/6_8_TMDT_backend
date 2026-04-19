package com.quangnt.ecom.controller;

import com.quangnt.common.builder.ResponseBuilder;
import com.quangnt.common.dto.ResponseDto;
import com.quangnt.common.enumeration.ResponseCode;
import com.quangnt.ecom.dto.LoginRequest;
import com.quangnt.ecom.dto.LoginResponse;
import com.quangnt.ecom.dto.Role;
import com.quangnt.ecom.dto.UserCreateRequest;
import com.quangnt.ecom.dto.UserResponse;
import com.quangnt.ecom.dto.UserUpdateRequest;
import com.quangnt.ecom.dto.VerifyCodeRequest;
import com.quangnt.ecom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ResponseDto<UserResponse>> create(@RequestBody UserCreateRequest request) {
        UserResponse response = userService.create(request, request.getRole());
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto<UserResponse>> register(@RequestBody UserCreateRequest request) {
        UserResponse response = userService.create(request, Role.USER);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<UserResponse>> update(@PathVariable String id, @RequestBody UserUpdateRequest request) {
        UserResponse response = userService.update(id, request);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseDto<Object>> getProfile() {
        UserResponse response = userService.getProfile();
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @DeleteMapping
    public ResponseEntity<ResponseDto<Object>> delete(@RequestBody List<String> ids) {
        userService.delete(ids);
        return ResponseBuilder.success(null, ResponseCode.SUCCESS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<UserResponse>> getOne(@PathVariable String id) {
        UserResponse response = userService.getOne(id);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<Page<UserResponse>>> search(Pageable pageable) {
        Page<UserResponse> response = userService.search(pageable);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<LoginResponse>> login(@RequestBody @Validated LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseBuilder.success(response, ResponseCode.SUCCESS);
    }

    @PostMapping("/verify-code")
    public ResponseEntity<ResponseDto<Void>> verifyCode(@RequestBody @Validated VerifyCodeRequest request) {
        userService.verifyCodeEmail(request.getCode(), request.getUserId());
        return ResponseBuilder.success(null, ResponseCode.SUCCESS);
    }
}
