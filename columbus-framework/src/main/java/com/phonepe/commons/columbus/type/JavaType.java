package com.phonepe.commons.columbus.type;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * A type of {@link EntityType} that uses a {@link TypeReference} to represent a type.
 *
 * @param <T> the type of the class this entity represents.
 */
@Getter
@AllArgsConstructor(staticName = "of")
public final class JavaType<T> implements EntityType<T> {

    @NonNull
    private final TypeReference<T> type;

    @Override
    public String name() {
        return type.getType().getTypeName();
    }

    @Override
    public <R, C> R accept(final Visitor<T, R, C> visitor, final C context) {
        return visitor.visit(this, context);
    }
}
