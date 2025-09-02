package org.acme.exception;

public class ErrorResponse {
    private Integer status;
    private String errorDetails;

    public ErrorResponse(Integer status, String errorDetails){
        this.status = status;
        this.errorDetails = errorDetails;
    }

    private Integer getStatus(){
        return this.status;
    }

    private String getErrorDetails(){
        return this.errorDetails;
    }
}
