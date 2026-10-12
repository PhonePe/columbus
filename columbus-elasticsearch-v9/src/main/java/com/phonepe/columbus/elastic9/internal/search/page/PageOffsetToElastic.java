package com.phonepe.columbus.elastic9.internal.search.page;

import com.phonepe.columbus.elastic9.search.page.ElasticPage;
import com.phonepe.columbus.elastic9.search.page.ElasticPageResolutionContext;
import com.phonepe.columbus.elastic9.search.page.PageToElastic;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.page.PageOffset;

@RegistryKey(name = PageOffset.TYPE, type = PageOffset.class)
public class PageOffsetToElastic<C extends ElasticPageResolutionContext<C>> implements PageToElastic<PageOffset, C> {

    @Override
    public ElasticPage visit(final PageOffset page, final Object props, final C context) {
        return ElasticPage.builder()
                .size(page.getSize())
                .from(page.getOffset())
                .build();
    }
}
