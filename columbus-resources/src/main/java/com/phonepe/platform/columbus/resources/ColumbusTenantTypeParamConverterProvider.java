package com.phonepe.platform.columbus.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.platform.columbus.models.core.ColumbusTenantType;
import lombok.RequiredArgsConstructor;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@RequiredArgsConstructor
public class ColumbusTenantTypeParamConverterProvider implements ParamConverterProvider {
    private final ObjectMapper mapper;
    private final Class<? extends ColumbusTenantType> entity;

    @Override
    @SuppressWarnings("unchecked")
    public <T> ParamConverter<T> getConverter(Class<T> paramType, Type type, Annotation[] annotations) {
        if (paramType.equals(ColumbusTenantType.class)) {
            return (ParamConverter<T>) new ColumbusTenantTypeParamConverter(mapper, entity);
        }
        return null;
    }
}
