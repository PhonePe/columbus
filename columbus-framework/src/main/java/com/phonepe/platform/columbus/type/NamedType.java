package com.phonepe.platform.columbus.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * A type of {@link EntityType} that uses a simple string name to identify a type.
 * <p>
 * {@link TypeResolver} can be used to find the {@link JavaType} corresponding to the given name.
 *
 * @param <T> the type of the class this entity represents.
 * @see TypeResolver
 */
@Data
@AllArgsConstructor(staticName = "of", onConstructor_ = @JsonCreator)
public final class NamedType<T> implements EntityType<T> {

    @NonNull
    @JsonValue
    private final String name;

    @Override
    public String name() {
        return name;
    }

    @Override
    public <R, C> R accept(final Visitor<T, R, C> visitor, final C context) {
        return visitor.visit(this, context);
    }
}
