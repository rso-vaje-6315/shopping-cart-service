package si.rso.cart.api.mappers;

import si.rso.rest.exceptions.RestException;
import si.rso.rest.exceptions.dto.ExceptionResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class DefaultExceptionMapper implements ExceptionMapper<RestException> {

    @Override
    public Response toResponse(RestException exception) {
        ExceptionResponse response = new ExceptionResponse();
        response.setMessage(exception.getMessage());
        response.setStatusCode(exception.getStatus());
        return Response.status(exception.getStatus()).entity(response).build();
    }

}