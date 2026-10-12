package com.phonepe.columbus.elastic7.internal.search.utils;

import lombok.experimental.UtilityClass;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FieldValueFactorFunction.Modifier;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery.ScoreMode;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.DeprecationHandler;
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.search.MatchQuery;
import org.elasticsearch.search.aggregations.Aggregator.SubAggCollectionMode;
import org.elasticsearch.search.sort.ScriptSortBuilder.ScriptSortType;
import org.elasticsearch.search.sort.SortMode;
import org.elasticsearch.search.sort.SortOrder;

import static com.phonepe.columbus.common.Transforms.toEnumValue;

/**
 * A collection of utilities to transform to ElasticSearch enums.
 */
@UtilityClass
public class SearchEnums {

    public SortOrder toSortOrder(final com.phonepe.columbus.search.sort.SortOrder order) {
        return switch (order) {
            case ASC -> SortOrder.ASC;
            case DESC -> SortOrder.DESC;
        };
    }

    public SortMode toSortMode(final String sortMode) {
        return toEnumValue(sortMode, "sortMode", SortMode::fromString);
    }

    public CombineFunction toBoostMode(final String boostMode) {
        return toEnumValue(boostMode, "boostMode", CombineFunction::fromString);
    }

    public ScoreMode toScoreMode(final String scoreMode) {
        return toEnumValue(scoreMode, "scoreMode", ScoreMode::fromString);
    }

    public MatchQuery.ZeroTermsQuery toZeroTermsQuery(final boolean returnAllForZeroTerms) {
        return returnAllForZeroTerms ? MatchQuery.ZeroTermsQuery.ALL : MatchQuery.ZeroTermsQuery.NONE;
    }

    public Operator toOperator(final boolean matchAll) {
        return matchAll ? Operator.AND : Operator.OR;
    }

    public Modifier toModifier(final String modifier) {
        return toEnumValue(modifier, "modifier", Modifier::fromString);
    }

    public Type toMultiMatchType(final String matchType) {
        return toEnumValue(matchType, "multiMatchType", value ->
                Type.parse(matchType, DeprecationHandler.IGNORE_DEPRECATIONS));
    }

    public GeoDistance toGeoDistance(final String distanceType) {
        return toEnumValue(distanceType, "distanceType", GeoDistance::fromString);
    }

    public DistanceUnit toDistanceUnit(final String distanceUnit) {
        return toEnumValue(distanceUnit, "distanceUnit", DistanceUnit::fromString);
    }

    public ScriptSortType toScriptSortType(final String valueType) {
        return toEnumValue(valueType, "scriptSortType", ScriptSortType::fromString);
    }

    public SubAggCollectionMode toSubAggregationCollectionMode(final String collectMode) {
        return toEnumValue(collectMode, "collectMode", value ->
                SubAggCollectionMode.parse(collectMode, DeprecationHandler.IGNORE_DEPRECATIONS));
    }
}
