package com.quangnt.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto<T> {
    private boolean success;
    private String message; // final message trả ra
    private T data;
    private Integer statusCode;
    private MetaData metaData;

    @JsonIgnore
    private String messageKey;

    @JsonIgnore
    private Object[] args;

    public ResponseDto(T data, MetaData metaData) {
        this.data = data;
        this.metaData = metaData;
        this.success = true;
        this.message = "SUCCESS";
        this.messageKey = "SUCCESS";
    }
}
