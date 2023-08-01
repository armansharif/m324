package com.dam.commons;

import org.springframework.http.HttpStatus;
public interface ExceptionType {
    HttpStatus getHttpStatus();
    int getErrorCode();
    String getMessageKey();
}
