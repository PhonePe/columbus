package com.phonepe.columbus.search.aggregator;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.phonepe.columbus.search.aggregator.bucket.FilterAggregator;
import com.phonepe.columbus.search.aggregator.bucket.FiltersAggregator;
import com.phonepe.columbus.search.aggregator.bucket.HistogramAggregator;
import com.phonepe.columbus.search.aggregator.bucket.RangeAggregator;
import com.phonepe.columbus.search.aggregator.bucket.SamplerAggregator;
import com.phonepe.columbus.search.aggregator.bucket.TermsAggregator;
import com.phonepe.columbus.search.aggregator.metrics.CardinalityAggregator;
import com.phonepe.columbus.search.aggregator.metrics.StatsAggregator;
import com.phonepe.columbus.search.aggregator.metrics.TopHitsAggregator;
import lombok.NonNull;

/**
 * An aggregator represents a way to summarize the documents by grouping them into buckets, applying mathematical
 * calculations, etc.
 * <p>
 * Each aggregator must be uniquely identified by its name.
 */
@JsonClassDescription("Interface for aggregators that summarize documents by grouping into buckets or calculating metrics")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
@JsonSubTypes({
        // Metrics aggregators
        @JsonSubTypes.Type(CardinalityAggregator.class),
        @JsonSubTypes.Type(StatsAggregator.class),
        @JsonSubTypes.Type(TopHitsAggregator.class),
        // Bucket aggregators
        @JsonSubTypes.Type(FilterAggregator.class),
        @JsonSubTypes.Type(FiltersAggregator.class),
        @JsonSubTypes.Type(HistogramAggregator.class),
        @JsonSubTypes.Type(RangeAggregator.class),
        @JsonSubTypes.Type(SamplerAggregator.class),
        @JsonSubTypes.Type(TermsAggregator.class)
})
public interface Aggregator {

    @NonNull
    @JsonPropertyDescription("Unique identifier name for this aggregator")
    String getName();

    @NonNull
    @JsonPropertyDescription("The type of aggregator")
    String getType();
}
