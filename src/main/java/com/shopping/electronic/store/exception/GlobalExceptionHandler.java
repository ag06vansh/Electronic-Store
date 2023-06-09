package com.shopping.electronic.store.exception;

import com.shopping.electronic.store.util.ApiResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// It will handle all exceptions in our program
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handler for our custom ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {

        ApiResponse response = ApiResponse.builder()
            .message(ex.getMessage())
            .status(HttpStatus.NOT_FOUND)
            .success(true)
            .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Handler for our custom MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        Map<String, Object> response = new HashMap<>();
        allErrors.forEach(objectError ->
            {
                String message = objectError.getDefaultMessage();
                String field = ((FieldError) objectError).getField();
                response.put(field, message);
            }
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadApiRequestException.class)
    public ResponseEntity<ApiResponse> handleBadApiRequestException(BadApiRequestException ex) {

        ApiResponse response = ApiResponse.builder()
            .message(ex.getMessage())
            .status(HttpStatus.BAD_REQUEST)
            .success(false)
            .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
