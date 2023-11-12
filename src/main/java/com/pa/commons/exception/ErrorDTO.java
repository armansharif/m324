package com.pa.commons.exception;

import lombok.Data;

@Data
public class ErrorDTO {

    private String timestamp;
    private String status;
    private String message;
}
