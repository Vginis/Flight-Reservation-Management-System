package org.acme.exception;

public class InvalidRequestException extends RuntimeException {
    private final ErrorResponse response;

    public InvalidRequestException(String message) {
        super(message);
        this.response = new ErrorResponse(404, message);
    }

    public ErrorResponse getErrorResponse(){
        return this.response;
    }
}
