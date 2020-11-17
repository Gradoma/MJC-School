package com.epam.esm.handler;

import com.epam.esm.exception.DuplicateException;
import com.epam.esm.exception.ResourceNotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.format.DateTimeParseException;
import java.util.Locale;

@ControllerAdvice
public class ResponseExceptionHandler{
    private final MessageSource messageSource;

    public ResponseExceptionHandler(MessageSource messageSource){
        this.messageSource = messageSource;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleResourceNotFoundException (ResourceNotFoundException ex,
                                                                                Locale locale) {
        String message = messageSource.getMessage("message.notFound", new Object[]{ex.getMessage()},
                locale);
        return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateException.class)
    public final ResponseEntity<ErrorResponse> handleDuplicateException (DuplicateException ex,
                                                                         Locale locale) {
        String message = messageSource.getMessage("message.duplicate", new Object[]{ex.getMessage()},
                locale);
        return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<ErrorResponse> handleConstraintViolationException (ConstraintViolationException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public final ResponseEntity<ErrorResponse> handleDateTimeParseException (DateTimeParseException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
