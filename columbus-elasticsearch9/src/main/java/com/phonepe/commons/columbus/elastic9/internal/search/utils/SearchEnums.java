package com.phonepe.commons.columbus.elastic9.internal.search.utils;

import co.elastic.clients.elasticsearch._types.DistanceUnit;
import co.elastic.clients.elasticsearch._types.FieldSortNumericType;
import co.elastic.clients.elasticsearch._types.GeoDistanceType;
import co.elastic.clients.elasticsearch._types.ScriptSortType;
import co.elastic.clients.elasticsearch._types.SortMode;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregationCollectMode;
import co.elastic.clients.elasticsearch._types.mapping.FieldType;
import co.elastic.clients.elasticsearch._types.query_dsl.FieldValueFactorModifier;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionBoostMode;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScoreMode;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch._types.query_dsl.ZeroTermsQuery;
import lombok.experimental.UtilityClass;

import static com.phonepe.commons.columbus.common.Transforms.toEnumValue;

/**
 * A collection of utilities to transform to ElasticSearch enums.
 */
@UtilityClass
public class SearchEnums {

    public SortOrder toSortOrder(final com.phonepe.commons.columbus.search.sort.SortOrder order) {
        return switch (order) {
            case ASC -> SortOrder.Asc;
            case DESC -> SortOrder.Desc;
        };
    }

    public SortMode toSortMode(final String sortMode) {
        return toEnumValue(sortMode, "sortMode", SortMode::valueOf);
    }

    public FunctionBoostMode toBoostMode(final String boostMode) {
        return toEnumValue(boostMode, "boostMode", FunctionBoostMode::valueOf);
    }

    public FunctionScoreMode toScoreMode(final String scoreMode) {
        return toEnumValue(scoreMode, "scoreMode", FunctionScoreMode::valueOf);
    }

    public FieldSortNumericType toFieldSortNumericType(final String numericType) {
        return toEnumValue(numericType, "numericType", FieldSortNumericType::valueOf);
    }

    public ZeroTermsQuery toZeroTermsQuery(final boolean returnAllForZeroTerms) {
        return returnAllForZeroTerms ? ZeroTermsQuery.All : ZeroTermsQuery.None;
    }

    public Operator toOperator(final boolean matchAll) {
        return matchAll ? Operator.And : Operator.Or;
    }

    public FieldValueFactorModifier toModifier(final String modifier) {
        return toEnumValue(modifier, "modifier", FieldValueFactorModifier::valueOf);
    }

    public TextQueryType toMultiMatchType(final String matchType) {
        return toEnumValue(matchType, "multiMatchType", TextQueryType::valueOf);
    }

    public GeoDistanceType toGeoDistance(final String distanceType) {
        return toEnumValue(distanceType, "distanceType", GeoDistanceType::valueOf);
    }

    public DistanceUnit toDistanceUnit(final String distanceUnit) {
        return toEnumValue(distanceUnit, "distanceUnit", DistanceUnit::valueOf);
    }

    public ScriptSortType toScriptSortType(final String valueType) {
        return toEnumValue(valueType, "scriptSortType", ScriptSortType::valueOf);
    }

    public TermsAggregationCollectMode toSubAggregationCollectionMode(final String collectMode) {
        return toEnumValue(collectMode, "collectMode", TermsAggregationCollectMode::valueOf);
    }

    public FieldType toUnmappedType(final String unmappedType) {
        return toEnumValue(unmappedType, "unmappedType", FieldType::valueOf);
    }

    public Time toTime(final long time) {
        return new Time.Builder().time(String.format("%ds", time)).build();
    }
}
