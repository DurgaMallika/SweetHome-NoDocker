package com.upgrad.Booking.exception.handler;

import com.upgrad.Booking.exception.InvalidInputException;
import com.upgrad.Booking.exception.RecordNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(RecordNotFoundException.class)
    public final ResponseEntity<Object> handleRecordNotFoundException(RecordNotFoundException e, WebRequest req){
        Map<String, Object> body = new LinkedHashMap<>();

        body.put("message", e.getLocalizedMessage());
        body.put("statusCode", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidInputException.class)
    public final ResponseEntity<Object> handleInvalidInputException(InvalidInputException ex, WebRequest webRequest){
        Map<String, Object> body = new LinkedHashMap<>();

        body.put("message", ex.getLocalizedMessage());
        body.put("statusCode", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity(body, HttpStatus.BAD_REQUEST);
    }
}

