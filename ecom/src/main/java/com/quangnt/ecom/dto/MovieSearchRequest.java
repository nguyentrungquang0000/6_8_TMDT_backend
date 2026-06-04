package com.quangnt.ecom.dto;

public record MovieSearchRequest (
        Integer page,
        Integer size,
        String keyword,
        MovieStatus status
) {
}
