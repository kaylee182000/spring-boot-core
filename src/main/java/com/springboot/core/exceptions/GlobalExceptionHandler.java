package com.springboot.core.exceptions;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.springboot.core.models.CommonResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    // private static final Logger logger =
    // LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CommonResponse<?>> handleResourceNotFoundException(NotFoundException ex,
            WebRequest request) {
        // logger.error("ResourceNotFoundException: {}", ex.getMessage());
        CommonResponse<?> response = CommonResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .success(false)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
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
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<?>> handleGlobalException(Exception ex, WebRequest request) {
        // logger.error("InternalException: {}", ex.getMessage());
        CommonResponse<?> response = CommonResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .success(false)
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
