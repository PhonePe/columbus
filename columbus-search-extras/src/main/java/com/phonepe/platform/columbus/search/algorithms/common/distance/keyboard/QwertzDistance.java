package com.phonepe.platform.columbus.search.algorithms.common.distance.keyboard;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@SuppressWarnings("SpellCheckingInspection")
public class QwertzDistance extends KeyboardDistance {
    @SuppressWarnings("SpellCheckingInspection")
    private static final Keyboard keyboard = new Keyboard(
        "qwertzuiopü",
        "asdfghjklöä",
        "yxcvbnm"
    );

    @Override
    protected Keyboard getKeyboard() {
        return keyboard;
    }
}
