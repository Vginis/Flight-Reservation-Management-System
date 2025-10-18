package org.acme.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.acme.representation.MessageRepresentation;

@Provider
public class InvalidRequestExceptionExceptionMapper implements ExceptionMapper<InvalidRequestException> {
    @Override
    public Response toResponse(InvalidRequestException exception) {
        return Response.status(400)
                .entity(new MessageRepresentation(exception.getMessage()))
                .build();
    }
}
