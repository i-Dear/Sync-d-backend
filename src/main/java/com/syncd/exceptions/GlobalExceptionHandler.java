package com.syncd.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import io.sentry.Sentry;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorDto> handleCustomException(CustomException ex) {
        ErrorInfo errorInfo = ex.getErrorInfo();
        logger.error(errorInfo.getMessage(), ex);
        Sentry.captureException(ex);
        return ResponseEntity
                .status(errorInfo.getStatus())
                .body(new ErrorDto(errorInfo.getDetailStatusCode(), ex.getDetailedMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        logger.error("Validation error: {}", errorMessage, ex);
        Sentry.captureException(ex);
        return ResponseEntity.badRequest().body(errorMessage);
    }
}
