package com.phonepe.commons.columbus.elastic.internal.search.page;

import com.phonepe.commons.columbus.elastic.search.page.ElasticPage;
import com.phonepe.commons.columbus.elastic.search.page.ElasticPageResolutionContext;
import com.phonepe.commons.columbus.elastic.search.page.PageToElastic;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.page.PagePointer;

import java.util.List;

@RegistryKey(name = PagePointer.TYPE, type = PagePointer.class)
public class PagePointerToElastic<C extends ElasticPageResolutionContext<C>> implements PageToElastic<PagePointer, C> {

    @Override
    public ElasticPage visit(final PagePointer page, final Object props, final C context) {
        return ElasticPage.builder()
                .size(page.getSize())
                .afterKeys(getKeys(page.getPointer()))
                .build();
    }

    private List<?> getKeys(final Object pointer) {
        if (pointer == null) {
            return List.of();
        }
        if (pointer instanceof List<?> keys) {
            return keys;
        }
        return List.of(pointer);
    }
}
