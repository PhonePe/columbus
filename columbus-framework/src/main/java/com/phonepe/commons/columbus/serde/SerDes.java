package com.phonepe.commons.columbus.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.commons.columbus.internal.serde.JsonSerDe;
import com.phonepe.commons.columbus.type.TypeResolver;
import lombok.experimental.UtilityClass;

/**
 * A factory class to create different types of serde.
 */
@UtilityClass
public class SerDes {

    public JsonSerDe json(final ObjectMapper mapper) {
        return JsonSerDe.builder()
                .mapper(mapper)
                .build();
    }

    public JsonSerDe json(final ObjectMapper mapper, final TypeResolver typeResolver) {
        return JsonSerDe.builder()
                .mapper(mapper)
                .typeResolver(typeResolver)
                .build();
    }
}
