package com.quangnt.common.exception;

import com.quangnt.common.enumeration.ResponseCode;
import lombok.Getter;

@Getter

public class BusinessException extends RuntimeException {
    private final ResponseCode code;
    private final Object[] args;

    public BusinessException(ResponseCode code, Object... args) {
        super(code.getMessageKey());
        this.code = code;
        this.args = args;
    }

    public BusinessException(ResponseCode code, String message) {
        super(message);
        this.code = code;
        this.args = null;
    }
}
