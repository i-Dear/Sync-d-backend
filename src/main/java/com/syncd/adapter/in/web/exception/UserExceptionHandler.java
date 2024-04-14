
package com.syncd.adapter.in.web.exception;

import com.syncd.adapter.in.web.exception.exceptions.LoginException;
import com.syncd.adapter.in.web.exception.exceptions.ProjectNotFoundException;
import com.syncd.adapter.in.web.exception.exceptions.ProjectOperationException;
import com.syncd.adapter.in.web.exception.exceptions.RegisterException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UserExceptionHandler{
    @ExceptionHandler(RegisterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleRegistrationException(RegisterException ex){
        return ex.getMessage();
    }
    @ExceptionHandler(LoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleLoginException(LoginException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(ProjectOperationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleProjectOperationException(ProjectOperationException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleProjectNotFoundException(ProjectNotFoundException ex) {
        return ex.getMessage();
    }
}
