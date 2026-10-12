package com.phonepe.columbus.search.algorithms.common.distance.keyboard;

import com.phonepe.columbus.search.algorithms.common.distance.Distance;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@SuppressWarnings("java:S1170")
public abstract class KeyboardDistance implements Distance<Character> {

    @Builder.Default
    private final double adjacent = 0.1f;
    @Builder.Default
    private final double diagonal = 0.4f;
    @Builder.Default
    private final double distant = 1f;

    @Override
    public double distance(@NonNull Character from, @NonNull Character to) {
        return getDistance(getKeyboard().getConnection(from, to));
    }

    protected abstract Keyboard getKeyboard();

    protected double getDistance(final Keyboard.Connection connection) {
        return switch (connection) {
            case SAME -> 0;
            case ADJACENT -> adjacent;
            case DIAGONAL -> diagonal;
            case DISTANT -> distant;
        };
    }
}
