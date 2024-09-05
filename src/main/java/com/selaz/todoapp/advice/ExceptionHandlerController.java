package com.selaz.todoapp.advice;

import com.selaz.todoapp.exceptions.NotAllowedException;
import com.selaz.todoapp.exceptions.ResourceAlreadyExistsException;
import com.selaz.todoapp.exceptions.ResourceNotFoundException;
import jakarta.xml.bind.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final String handleGeneric(Exception exception, WebRequest request) {
        return exception.getMessage();
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public final String handleResourceAlreadyExists(ResourceAlreadyExistsException exception, WebRequest request) {
        return exception.getMessage();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final String handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest request) {
        return exception.getMessage();
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected String handleBadCredentialsException(BadCredentialsException exception, WebRequest request) {
        return "User not exists or wrong password";
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected String handleValidationException(ValidationException exception, WebRequest request) {
        return exception.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected String handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, WebRequest request) {
        return exception.getMessage();
    }

    @ExceptionHandler(NotAllowedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected String handleNotAllowedException(NotAllowedException exception, WebRequest request) {
        return exception.getMessage();
    }
}
