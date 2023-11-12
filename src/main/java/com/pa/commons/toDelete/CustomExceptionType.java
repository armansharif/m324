package com.pa.commons.toDelete;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@Builder
public class CustomExceptionType implements ExceptionType {
    private HttpStatus httpStatus;
    private int errorCode;
    private String messageKey;
}
