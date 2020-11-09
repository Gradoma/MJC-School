package com.epam.esm.exception.handler;

import com.epam.esm.exception.DuplicateException;
import com.epam.esm.exception.ResourceNotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.logging.Logger;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {
    private final MessageSource messageSource;

    public ResponseExceptionHandler(MessageSource messageSource){
        this.messageSource = messageSource;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleResourceNotFoundException (ResourceNotFoundException ex,
                                                                         WebRequest request) {
        String message = messageSource.getMessage("message.notFound", new Object[]{ex.getMessage()},
                request.getLocale());
        return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateException.class)
    public final ResponseEntity<ErrorResponse> handleDuplicateException (DuplicateException ex,
                                                                         WebRequest request) {
        String message = messageSource.getMessage("message.duplicate", new Object[]{ex.getMessage()},
                request.getLocale());
        return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<ErrorResponse> handleConstraintViolationException (ConstraintViolationException ex,
                                                                         WebRequest request) {
        // work ok
        String exceptionMessage = ex.getMessage();
        System.out.println(exceptionMessage);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);

        // doesn't work (500, no handling)
        
//        String exceptionMessage = ex.getMessage();
//        int startIndex = exceptionMessage.indexOf("{");
//        int endIndex = exceptionMessage.indexOf("}");
//        String messageName = exceptionMessage.substring(startIndex, endIndex);
//        System.out.println(messageName);
//        String message = messageSource.getMessage(messageName, null,
//                request.getLocale());
//        return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.BAD_REQUEST);
    }
}
