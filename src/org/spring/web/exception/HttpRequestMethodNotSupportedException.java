package org.spring.web.exception;

public class HttpRequestMethodNotSupportedException extends RuntimeException{
    public HttpRequestMethodNotSupportedException() {
    }

    public HttpRequestMethodNotSupportedException(String message) {
        super(message);
    }
}
