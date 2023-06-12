package org.spring.security.data.exception;

public class SpringSecurityException extends RuntimeException{
    public SpringSecurityException() {
    }

    public SpringSecurityException(String message) {
        super(message);
    }
}
