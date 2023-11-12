package com.pa.commons.exception;
 

import java.util.Date;

 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
 
@ControllerAdvice
 
public class GlobalExceptionHandlar {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorDTO> generateException(ResponseStatusException re)
    {
        ErrorDTO dto = new ErrorDTO();
        dto.setTimestamp(new Date().toString());
        dto.setStatus( String.valueOf( re.getStatus().value()));
        dto.setMessage(re.getReason() );
        return new ResponseEntity<ErrorDTO>(dto,re.getStatus());
    }



    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDTO> generateException(RuntimeException re)
    {
        ErrorDTO dto = new ErrorDTO();
        dto.setTimestamp(new Date().toString());
        dto.setStatus("500");
        dto.setMessage(re.getMessage());

        return new ResponseEntity<ErrorDTO>(dto,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}