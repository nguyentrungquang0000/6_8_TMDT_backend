package com.quangnt.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaCreateRequest {
    private String fileName;
    private String fileUrl;
    private String fileType;
    private String entityType;
    private Integer entityId;
}