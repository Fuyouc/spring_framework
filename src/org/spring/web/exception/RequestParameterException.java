package org.spring.web.exception;

public class RequestParameterException extends RuntimeException{
    public RequestParameterException() {
    }

    public RequestParameterException(String message) {
        super(message);
    }
}
