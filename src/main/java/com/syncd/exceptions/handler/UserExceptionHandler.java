package com.syncd.exceptions.handler;

import com.syncd.exceptions.*;

import com.syncd.exceptions.enums.ExceptionType;
import com.syncd.exceptions.enums.LogMessageType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@ControllerAdvice
public class UserExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(UserExceptionHandler.class);

    @ExceptionHandler(RegisterException.class)
    public ResponseEntity<String> handleRegistrationException(RegisterException ex) {
        logger.error(LogMessageType.REGISTER_ERROR.getMessage(), ex);
        return ResponseEntity.badRequest().body(ExceptionType.REGISTER_ERROR.getMessage());
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<String> handleLoginException(LoginException ex) {
        logger.error(LogMessageType.LOGIN_ERROR.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionType.LOGIN_ERROR.getMessage());
    }

    @ExceptionHandler(ProjectOperationException.class)
    public ResponseEntity<String> handleProjectOperationException(ProjectOperationException ex) {
        logger.error(LogMessageType.PROJECT_OPERATION_ERROR.getMessage(), ex);
        return ResponseEntity.badRequest().body(ExceptionType.PROJECT_OPERATION_ERROR.getMessage());
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<String> handleProjectNotFoundException(ProjectNotFoundException ex) {
        logger.error(LogMessageType.PROJECT_NOT_FOUND.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionType.PROJECT_NOT_FOUND.getMessage());
    }
}
