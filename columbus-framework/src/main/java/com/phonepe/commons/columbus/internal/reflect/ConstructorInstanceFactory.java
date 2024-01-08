package com.phonepe.commons.columbus.internal.reflect;

import com.phonepe.commons.columbus.reflect.InstanceFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * Uses the default constructor to create an instance.
 */
@Slf4j
public class ConstructorInstanceFactory implements InstanceFactory {

    @Override
    public <T> T getInstance(final Class<T> type) {
        try {
            return type.getConstructor().newInstance();
        } catch (Exception e) {
            log.error("Failed to create instance for {}", type, e);
            throw new IllegalArgumentException("Failed to create instance for " + type.getName(), e);
        }
    }
}
