package com.epam.esm.handler;

import com.epam.esm.exception.DuplicateException;
import com.epam.esm.exception.InvalidSortingException;
import com.epam.esm.exception.ResourceNotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.format.DateTimeParseException;
import java.util.Locale;

@ControllerAdvice
@Component
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {
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

    @ExceptionHandler(InvalidSortingException.class)
    public final ResponseEntity<ErrorResponse> handleInvalidSortingOrderException (InvalidSortingException ex,
                                                                                   Locale locale) {
        String message = messageSource.getMessage("message.sorting", new Object[]{ex.getMessage()},
                locale);
        return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConversionFailedException.class)          //todo fix
    public final ResponseEntity<ErrorResponse> handleConversionFailedException(ConversionFailedException ex){
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)       //todo fix
    public final ResponseEntity<ErrorResponse> handleConstraintViolationException (ConstraintViolationException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public final ResponseEntity<ErrorResponse> handleDateTimeParseException (DateTimeParseException ex, Locale locale) {
        String message = messageSource.getMessage("message.parsing", new Object[]{ex.getMessage()},
                locale);
        return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.BAD_REQUEST);
    }
}
