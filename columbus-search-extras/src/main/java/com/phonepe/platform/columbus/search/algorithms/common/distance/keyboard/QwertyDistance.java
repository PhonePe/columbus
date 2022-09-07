package com.phonepe.platform.columbus.search.algorithms.common.distance.keyboard;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public class QwertyDistance extends KeyboardDistance {
    @SuppressWarnings("SpellCheckingInspection")
    private static final Keyboard keyboard = new Keyboard(
        "qwertyuiop",
        "asdfghjkl",
        "zxcvbnm"
    );

    @Override
    protected Keyboard getKeyboard() {
        return keyboard;
    }
}
