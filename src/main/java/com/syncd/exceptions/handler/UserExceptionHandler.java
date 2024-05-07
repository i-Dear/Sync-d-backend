package com.syncd.exceptions.handler;
import com.syncd.exceptions.*;
import com.syncd.exceptions.enums.ExceptionType;
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
        logger.error(ExceptionType.REGISTER_ERROR.getMessage(), ex);
        return ResponseEntity.status(ExceptionType.REGISTER_ERROR.getStatus())
                .body(ExceptionType.REGISTER_ERROR.getMessage());
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<String> handleLoginException(LoginException ex) {
        logger.error(ExceptionType.LOGIN_ERROR.getMessage(), ex);
        return ResponseEntity.status(ExceptionType.LOGIN_ERROR.getStatus())
                .body(ExceptionType.LOGIN_ERROR.getMessage());
    }

    @ExceptionHandler(ProjectOperationException.class)
    public ResponseEntity<String> handleProjectOperationException(ProjectOperationException ex) {
        logger.error(ExceptionType.PROJECT_OPERATION_ERROR.getMessage(), ex);
        return ResponseEntity.status(ExceptionType.PROJECT_OPERATION_ERROR.getStatus())
                .body(ExceptionType.PROJECT_OPERATION_ERROR.getMessage());
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<String> handleProjectNotFoundException(ProjectNotFoundException ex) {
        logger.error(ExceptionType.PROJECT_NOT_FOUND.getMessage(), ex);
        return ResponseEntity.status(ExceptionType.PROJECT_NOT_FOUND.getStatus())
                .body(ExceptionType.PROJECT_NOT_FOUND.getMessage());
    }

    @ExceptionHandler(ProjectAlreadyExistsException.class)
    public ResponseEntity<String> handleProjectAlreadyExistsException(ProjectAlreadyExistsException ex) {
        logger.error(ExceptionType.PROJECT_ALREADY_EXISTS.getMessage(), ex);
        return ResponseEntity.status(ExceptionType.PROJECT_ALREADY_EXISTS.getStatus())
                .body(ExceptionType.PROJECT_ALREADY_EXISTS.getMessage());
    }
}
