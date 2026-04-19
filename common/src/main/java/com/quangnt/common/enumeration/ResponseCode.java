package com.quangnt.common.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    SUCCESS(200, "success"),

    /// ===== AUTH / SECURITY (1000) =====

    UNAUTHORIZED(1401, "error.auth.unauthorized"),
    ACCESS_DENIED(1403, "error.auth.access-denied"),
    TOKEN_EXPIRED(1404, "error.auth.token-expired"),
    AUTHORIZATION_DENIED(1405, "error.auth.authorization-denied"),

    /// ===== VALIDATION (2000) =====

    VALIDATION_FAILED(2000, "error.validation.failed"),
    INVALID_PARAMETER(2001, "error.validation.invalid-parameter"),
    MISSING_PARAMETER(2002, "error.validation.missing-parameter"),
    TYPE_MISMATCH(2003, "error.validation.type-mismatch"),
    MALFORMED_JSON(2004, "error.validation.malformed-json"),
    JSON_MAPPING_ERROR(2005, "error.validation.json-mapping"),

    /// ===== REQUEST / HTTP (3000) =====

    METHOD_NOT_SUPPORTED(3000, "error.http.method-not-supported"),
    MEDIA_TYPE_NOT_SUPPORTED(3001, "error.http.media-type-not-supported"),
    BAD_REQUEST(3002, "error.http.bad-request"),

    /// ===== BUSINESS (4000) =====
    MEDIA_NOTFOUND(404, "File không khả dụng"),
    BUSINESS_ERROR(4000, "error.business.general"),
    NOT_FOUND(4004, "error.resource.not-found"),

    // USER
    LOGIN_FAILED(400, "Sai thông tin đăng nhập"),
    EMAIL_EXISTED(400, "Email đã tồn tại"),
    ACCOUNT_LOCKED(400, "Tài khoản đã bị khóa"),
    /// ===== SYSTEM / FATAL (5000) =====

    SOMETHING_WENT_WRONG(500, "error.fatal.something-went-wrong"),
    INTERNAL_SERVER_ERROR(500, "error.fatal.internal-server-error"),
    ENTITY_NOT_FOUND(404, "Thực thể không tồn tại");

    private final int statusCode;
    private final String messageKey;

    public static String findMessageKeyByCode(int code) {
        for (ResponseCode c : values()) {
            if (c.statusCode == code) {
                return c.messageKey;
            }
        }
        return "System Error";
    }

    public static ResponseCode fromCode(int code) {
        for (ResponseCode c : values()) {
            if (c.statusCode == code) {
                return c;
            }
        }
        return INTERNAL_SERVER_ERROR;
    }
}
