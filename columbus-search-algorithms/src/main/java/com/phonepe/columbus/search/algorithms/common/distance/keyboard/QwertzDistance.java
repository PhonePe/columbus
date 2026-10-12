package com.phonepe.columbus.search.algorithms.common.distance.keyboard;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@SuppressWarnings("SpellCheckingInspection")
public class QwertzDistance extends KeyboardDistance {

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
