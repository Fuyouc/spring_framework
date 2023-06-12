package org.spring.web.file.exception;

public class NotAllowedUploadMultipleFile extends RuntimeException{
    public NotAllowedUploadMultipleFile() {
    }

    public NotAllowedUploadMultipleFile(String message) {
        super(message);
    }
}
