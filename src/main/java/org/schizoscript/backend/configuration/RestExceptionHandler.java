package org.schizoscript.backend.configuration;

import org.schizoscript.backend.dtos.ErrorDto;
import org.schizoscript.backend.exceptions.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class RestExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = {AppException.class})
    public ResponseEntity<ErrorDto> handleException(AppException exception) {
        return ResponseEntity
                .status(exception.getStatusCode())
                .body(ErrorDto.builder().message(exception.getMessage()).build());
    }
}
