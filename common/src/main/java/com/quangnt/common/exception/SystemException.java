package com.quangnt.common.exception;

public class SystemException extends RuntimeException {

    public SystemException (Throwable cause) {
        super(cause);
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
