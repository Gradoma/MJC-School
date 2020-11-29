package com.epam.esm.exception;

public class HibernateInitializationException extends RuntimeException {
    public HibernateInitializationException() {
    }

    public HibernateInitializationException(String message) {
        super(message);
    }

    public HibernateInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public HibernateInitializationException(Throwable cause) {
        super(cause);
    }
}
