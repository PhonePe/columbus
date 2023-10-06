package com.phonepe.platform.columbus.dao.id;

import lombok.experimental.UtilityClass;

/**
 * A factory class to create different types of {@link Id} instances.
 */
@UtilityClass
public class Ids {

    private static final AutoId AUTO_ID = AutoId.builder().build();

    public ValueId value(final String value) {
        return ValueId.builder()
                .value(value)
                .build();
    }

    public AutoId auto() {
        return AUTO_ID;
    }
}
