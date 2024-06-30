package com.ahmed.controller.exceptionHandler;

import com.ahmed.dto.InputFailedValidationResponse;
import com.ahmed.exception.InputValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class InputValidationHandler {

    @ExceptionHandler(InputValidationException.class)
    public ResponseEntity<InputFailedValidationResponse> handleException(InputValidationException ex) {
        InputFailedValidationResponse inputFailedValidationResponse = new InputFailedValidationResponse();
        inputFailedValidationResponse.setInput(ex.getInput());
        inputFailedValidationResponse.setMessage(ex.getMessage());
        inputFailedValidationResponse.setErrorCode(ex.getErrorCode());


        return ResponseEntity.badRequest().body(inputFailedValidationResponse);
    }
}
