package com.dam.commons;

import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public enum DamExceptionType implements ExceptionType {

    CUSTOMER_NOT_FOUND(HttpStatus.NOT_FOUND, 23001, "bank.account.customer.notfound"),
    CUSTOMER_NOT_ACTIVE(HttpStatus.BAD_REQUEST, 23002, "bank.account.change.customer.notActive"),
    CUSTOMER_OPERATION_NOT_AUTHORIZED(HttpStatus.BAD_REQUEST, 23003, "bank.account.customer.operation.notAuthorized"),
    NOT_HANDEL_EXCEPTION(HttpStatus.NOT_FOUND, 23008, "NOT_HANDEL_EXCEPTION");


    private final CustomExceptionType customExceptionType;

    DamExceptionType(HttpStatus httpStatus, int errorCode, String messageKey) {
        this.customExceptionType = new CustomExceptionType(httpStatus, errorCode, messageKey);
    }

    public static CustomExceptionType of(String messageKey) {
        Optional<DamExceptionType> damExceptionType = Arrays.stream(values())
                .filter(a -> Objects.equals(a.getMessageKey(), messageKey))
                .findFirst();

        if (damExceptionType.isPresent()) {
            return damExceptionType.get().getCustomExceptionType();
        } else {
            return NOT_HANDEL_EXCEPTION.getCustomExceptionType();
        }

    }

    @Override
    public HttpStatus getHttpStatus() {
        return customExceptionType.getHttpStatus();
    }

    @Override
    public int getErrorCode() {
        return customExceptionType.getErrorCode();
    }

    @Override
    public String getMessageKey() {
        return customExceptionType.getMessageKey();
    }

    public CustomExceptionType getCustomExceptionType() {
        return customExceptionType;
    }
}
