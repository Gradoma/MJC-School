package com.epam.esm.exception;

public class InvalidSortingException extends RuntimeException{
    public InvalidSortingException() {
    }

    public InvalidSortingException(String message) {
        super(message);
    }

    public InvalidSortingException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSortingException(Throwable cause) {
        super(cause);
    }
}
