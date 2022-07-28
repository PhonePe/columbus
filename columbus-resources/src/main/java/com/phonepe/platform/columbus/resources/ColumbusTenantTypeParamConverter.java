package com.phonepe.platform.columbus.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.platform.columbus.models.core.ColumbusTenantType;
import lombok.RequiredArgsConstructor;

import javax.ws.rs.ext.ParamConverter;

@RequiredArgsConstructor
public class ColumbusTenantTypeParamConverter implements ParamConverter<ColumbusTenantType> {
    private final ObjectMapper mapper;
    private final Class<? extends ColumbusTenantType> entity;

    @Override
    public ColumbusTenantType fromString(String s) {
        return mapper.convertValue(s, entity);
    }

    @Override
    public String toString(ColumbusTenantType columbusTenantType) {
        return columbusTenantType.name();
    }
}
