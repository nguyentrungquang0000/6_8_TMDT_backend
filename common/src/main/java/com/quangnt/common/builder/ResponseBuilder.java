package com.quangnt.common.builder;

import com.quangnt.common.dto.MetaData;
import com.quangnt.common.dto.ResponseDto;
import com.quangnt.common.enumeration.ResponseCode;
import jakarta.annotation.Nonnull;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;

@UtilityClass
public class ResponseBuilder {

    public static <T> ResponseEntity<ResponseDto<T>> success(
        T data,
        ResponseCode code
    ) {
        return build(true, null, data, code, null);
    }

    public static <T> ResponseEntity<ResponseDto<T>> success(
        T data,
        ResponseCode code,
        Object... args
    ) {
        return build(true, args, data, code, null);
    }

    public static <T> ResponseEntity<ResponseDto<T>> success(
        T data,
        ResponseCode code,
        MetaData metaData
    ) {
        return build(true, null, data, code, metaData);
    }

    // ===== ERROR =====

    public static <T> ResponseEntity<ResponseDto<T>> error(
        ResponseCode code
    ) {
        return build(false, null, null, code, null);
    }

    public static <T> ResponseEntity<ResponseDto<T>> error(
        ResponseCode code,
        Object... args
    ) {
        return build(false, args, null, code, null);
    }

    public static <T> ResponseEntity<ResponseDto<T>> error(
        T data,
        ResponseCode code
    ) {
        return build(false, null, data, code, null);
    }

    public static <T> ResponseEntity<ResponseDto<T>> error(
        ResponseCode code,
        String message
    ) {

        return build(false, null, (T) message, code, null);
    }

    private static int getStatusCode(ResponseCode code) {
        return code.getStatusCode();
    }

    // ===== CORE BUILD =====

    private static <T> ResponseEntity<ResponseDto<T>> build(
            boolean success,
            Object[] args,
            T data,
            ResponseCode code,
            MetaData metaData
    ) {

        ResponseDto<T> dto = ResponseDto.<T>builder()
                .success(success)
                .message(code.getMessageKey())
                .args(args)
                .data(data)
                .statusCode(code.getStatusCode())
                .metaData(metaData)
                .build();

        return ResponseEntity
                .status(code.getStatusCode())
                .body(dto);
    }


    // ===== DOWNLOAD =====

    @Nonnull
    public static ResponseEntity<InputStreamResource> download(
        @Nonnull InputStreamResource resource,
        @Nonnull String fileName) {

        return ResponseEntity.ok()
            .contentType(APPLICATION_OCTET_STREAM)
            .header(CONTENT_DISPOSITION,
                "attachment; filename=" + fileName)
            .body(resource);
    }
}
