package com.quangnt.ecom.dto;

import org.springframework.web.multipart.MultipartFile;

public record FileUploadRequest(
        MultipartFile file
) {
}
