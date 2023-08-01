package com.dam.commons;


import org.springframework.http.HttpStatus;

public class CommonHandledException extends RuntimeException {
    private final ExceptionType exceptionType;
    private final Object[] messageParams;

    public CommonHandledException(HttpStatus httpStatus, String messageKey, int errorCode, Throwable cause) {
        this(httpStatus, messageKey, errorCode, null, cause);
    }

    public CommonHandledException(HttpStatus httpStatus, String messageKey, int errorCode, Object[] messageParams, Throwable cause) {
        this(new CustomExceptionType(httpStatus, errorCode, messageKey), messageParams, cause);
    }

    public CommonHandledException(ExceptionType exceptionType) {
        this(exceptionType, null, null);
    }

    public CommonHandledException(ExceptionType exceptionType, Object[] messageParams) {
        this(exceptionType, messageParams, null);
    }

    public CommonHandledException(ExceptionType exceptionType, Object[] messageParams, Throwable cause) {
        super(cause);
        this.exceptionType = exceptionType;
        this.messageParams = messageParams;
    }

    public String getMessageKey() {
        return exceptionType.getMessageKey();
    }

    public int getErrorCode() {
        return exceptionType.getErrorCode();
    }

    public HttpStatus getHttpStatus() {
        return exceptionType.getHttpStatus();
    }

    public ExceptionType getExceptionType() {
        return exceptionType;
    }

    public Object[] getMessageParams() {
        return messageParams;
    }
}
