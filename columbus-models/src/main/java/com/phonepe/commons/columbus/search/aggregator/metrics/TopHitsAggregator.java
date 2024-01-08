package com.phonepe.commons.columbus.search.aggregator.metrics;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.aggregator.BaseAggregator;
import com.phonepe.commons.columbus.search.misc.query.Projection;
import com.phonepe.commons.columbus.search.sort.Sort;
import com.phonepe.commons.columbus.type.EntityType;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(TopHitsAggregator.TYPE)
public class TopHitsAggregator<T> extends BaseAggregator {

    public static final String TYPE = "TOP_HITS";

    @Builder.Default
    private int from = 0;
    @Builder.Default
    private int size = 3;

    @Singular(ignoreNullCollections = true)
    private List<@NonNull Sort> sorts;

    private Projection projection;
    private EntityType<T> entityType;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
