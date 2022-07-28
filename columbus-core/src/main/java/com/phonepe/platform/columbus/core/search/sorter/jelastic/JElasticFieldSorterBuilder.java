package com.phonepe.platform.columbus.core.search.sorter.jelastic;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.grookage.jelastic.core.models.query.sorter.Sorter;
import com.phonepe.platform.columbus.core.utils.JElasticSorterUtils;
import com.phonepe.platform.columbus.models.core.search.sorter.FieldSorter;

@JsonTypeName(FieldSorter.TYPE)
public class JElasticFieldSorterBuilder implements JElasticSorterBuilder<FieldSorter> {
    @Override
    public Sorter visit(FieldSorter sorter, JElasticSorterBuilderContext context) {
        return JElasticSorterUtils.fieldSorter(
            sorter.getField(),
            JElasticSorterUtils.sortOrder(sorter.getOrder()),
            sorter.getPriority()
        );
    }
}
