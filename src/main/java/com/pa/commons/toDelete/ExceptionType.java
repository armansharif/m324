package com.pa.commons.toDelete;

import org.springframework.http.HttpStatus;
public interface ExceptionType {
    HttpStatus getHttpStatus();
    int getErrorCode();
    String getMessageKey();
}
