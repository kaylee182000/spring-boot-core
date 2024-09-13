package com.springboot.core.exceptions;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.springboot.core.models.CommonResponse;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CommonResponse<?>> handleResourceNotFoundException(ResourceNotFoundException ex,
            WebRequest request) {
        // logger.error("ResourceNotFoundException: {}", ex.getMessage());
        CommonResponse<?> response = CommonResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .success(false)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CommonResponse<?>> handleAuthenticationException(AuthenticationException ex,
            WebRequest request) {
        // logger.error("AuthenticationException: {}", ex.getMessage());
        CommonResponse<?> response = CommonResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .message(ex.getMessage())
                // .message("BAD_CREDENTIALS")
                .success(false)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
            WebRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        CommonResponse<?> response = CommonResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .success(false)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<CommonResponse<?>> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex,
            WebRequest request) {
        CommonResponse<?> response = CommonResponse.builder()
                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .message(ex.getMessage())
                .success(false)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<CommonResponse<?>> handleHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException ex,
            WebRequest request) {
        CommonResponse<?> response = CommonResponse.builder()
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                .message(ex.getMessage())
                .success(false)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CommonResponse<?>> handleConstraintViolationException(ConstraintViolationException ex,
            WebRequest request) {
        String errorMessage = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
        CommonResponse<?> response = CommonResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .success(false)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<?>> handleGlobalException(Exception ex, WebRequest request) {
        // logger.error("InternalException: {}", ex.getMessage());
        CommonResponse<?> response = CommonResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .success(false)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}