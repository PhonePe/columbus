package com.phonepe.platform.columbus.elastic.internal.search.page;

import com.phonepe.platform.columbus.elastic.search.page.ElasticPage;
import com.phonepe.platform.columbus.elastic.search.page.ElasticPageResolutionContext;
import com.phonepe.platform.columbus.elastic.search.page.PageToElastic;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.page.PageOffset;

@RegistryKey(name = PageOffset.TYPE, type = PageOffset.class)
public class PageOffsetToElastic<C extends ElasticPageResolutionContext<C>> implements PageToElastic<PageOffset, C> {

    @Override
    public ElasticPage visit(final PageOffset page, final C context) {
        return ElasticPage.builder()
                .size(page.getSize())
                .from(page.getOffset())
                .build();
    }
}
