package com.quangnt.ecom.controller;

import com.quangnt.ecom.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/v1")
public class CheckTestController {
    @GetMapping("/check")
    public User check(){
        return User.builder()
                .id("1")
                .createdAt(OffsetDateTime.now())
                .build();
    }
}
