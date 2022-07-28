package com.phonepe.platform.columbus.core.exception;

import com.phonepe.platform.columbus.models.core.exceptions.ColumbusError;
import lombok.Getter;

import java.util.Map;

@Getter
public class ColumbusException extends RuntimeException {
    private static final String MSG = "message";

    private final ColumbusError errorCode;
    private final transient Map<String, Object> context;

    private ColumbusException(ColumbusError errorCode, Map<String, Object> context) {
        super();
        this.errorCode = errorCode;
        this.context = context;
    }

    private ColumbusException(Throwable cause, ColumbusError errorCode) {
        super(cause);
        this.errorCode = errorCode;
        this.context = Map.of(MSG, String.valueOf(cause.getLocalizedMessage()));
    }

    private ColumbusException(Throwable cause, String message, ColumbusError errorCode) {
        super(cause);
        this.errorCode = errorCode;
        this.context = Map.of(MSG, message);
    }

    private ColumbusException(ColumbusError errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.context = Map.of(MSG, message);
    }

    public static ColumbusException propagate(ColumbusError errorCode, Throwable t) {
        if (t instanceof ColumbusException columbusException) {
            return columbusException;
        }
        return new ColumbusException(t, errorCode);
    }

    public static ColumbusException propagate(ColumbusError errorCode, String message, Throwable t) {
        if (t instanceof ColumbusException columbusException) {
            return columbusException;
        }
        return new ColumbusException(t, message, errorCode);
    }

    public static ColumbusException error(ColumbusError errorCode, Map<String, Object> context) {
        return new ColumbusException(errorCode, context);
    }

    public static ColumbusException error(ColumbusError errorCode, String message) {
        return new ColumbusException(errorCode, message);
    }
}
