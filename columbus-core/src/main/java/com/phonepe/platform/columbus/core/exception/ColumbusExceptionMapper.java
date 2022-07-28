package com.phonepe.platform.columbus.core.exception;

import com.phonepe.models.response.GenericResponse;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@Slf4j
public class ColumbusExceptionMapper implements ExceptionMapper<ColumbusException> {
    @Override
    public Response toResponse(ColumbusException exception) {
        log.error("ERROR", exception);

        return Response.status(500)
            .entity(GenericResponse.builder()
                .success(false)
                .code(exception.getErrorCode().name())
                .message(exception.getMessage())
                .data(exception.getContext())
                .build())
            .build();
    }
}
