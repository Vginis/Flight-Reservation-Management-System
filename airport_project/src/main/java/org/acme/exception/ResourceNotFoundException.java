package org.acme.exception;

public class ResourceNotFoundException extends RuntimeException{
    private final ErrorResponse response;

    public ResourceNotFoundException(String message) {
        super(message);
        this.response = new ErrorResponse(404, message);
    }

    public ErrorResponse getErrorResponse(){
        return this.response;
    }
}
