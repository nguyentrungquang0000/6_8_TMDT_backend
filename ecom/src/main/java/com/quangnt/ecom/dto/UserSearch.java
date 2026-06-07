package com.quangnt.ecom.dto;

public record UserSearch(
        Integer page,
        Integer size,
        String keyword,
        Role role,
        Boolean isLock
) {
}
