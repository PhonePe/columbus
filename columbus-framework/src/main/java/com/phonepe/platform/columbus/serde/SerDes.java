package com.phonepe.platform.columbus.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.platform.columbus.internal.serde.JsonSerDe;
import com.phonepe.platform.columbus.type.TypeResolver;
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
