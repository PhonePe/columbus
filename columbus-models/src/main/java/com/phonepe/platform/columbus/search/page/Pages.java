package com.phonepe.platform.columbus.search.page;

import lombok.experimental.UtilityClass;

/**
 * A factory class to create different types of {@link PageBounds} instances.
 */
@UtilityClass
public class Pages {

    private static final PageBounds EMPTY = offset(0, 0);

    public PageBounds empty() {
        return EMPTY;
    }

    public PageNumber number(final int size, final int number) {
        return PageNumber.builder()
                .size(size)
                .number(number)
                .build();
    }

    public PageOffset offset(final int size, final int offset) {
        return PageOffset.builder()
                .size(size)
                .offset(offset)
                .build();
    }

    public PagePointer pointer(final int size, final Object pointer) {
        return PagePointer.builder()
                .size(size)
                .pointer(pointer)
                .build();
    }
}
