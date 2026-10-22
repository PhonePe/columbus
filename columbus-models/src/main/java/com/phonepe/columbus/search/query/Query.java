/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phonepe.columbus.search.query;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.phonepe.columbus.search.filter.general.EqualsFilter;
import com.phonepe.columbus.search.filter.general.InFilter;
import com.phonepe.columbus.search.filter.predicate.AndFilter;
import com.phonepe.columbus.search.filter.predicate.NotFilter;
import com.phonepe.columbus.search.filter.predicate.OrFilter;
import com.phonepe.columbus.search.filter.range.BetweenFilter;
import com.phonepe.columbus.search.filter.range.GreaterThanEqualsFilter;
import com.phonepe.columbus.search.filter.range.GreaterThanFilter;
import com.phonepe.columbus.search.filter.range.LessThanEqualsFilter;
import com.phonepe.columbus.search.filter.range.LessThanFilter;
import com.phonepe.columbus.search.filter.score.FixedScoreFilter;
import com.phonepe.columbus.search.filter.string.TextMatchFilter;
import com.phonepe.columbus.search.query.compound.BoolQuery;
import com.phonepe.columbus.search.query.compound.BoostingQuery;
import com.phonepe.columbus.search.query.compound.ConstantScoreQuery;
import com.phonepe.columbus.search.query.compound.DisMaxQuery;
import com.phonepe.columbus.search.query.compound.FunctionScoreQuery;
import com.phonepe.columbus.search.query.geo.GeoDistanceQuery;
import com.phonepe.columbus.search.query.geo.GeoPointQuery;
import com.phonepe.columbus.search.query.simple.MatchAllQuery;
import com.phonepe.columbus.search.query.simple.MatchNoneQuery;
import com.phonepe.columbus.search.query.special.KnnQuery;
import com.phonepe.columbus.search.query.special.ScriptQuery;
import com.phonepe.columbus.search.query.term.FieldExistsQuery;
import com.phonepe.columbus.search.query.term.FuzzyQuery;
import com.phonepe.columbus.search.query.term.IdsQuery;
import com.phonepe.columbus.search.query.term.PrefixQuery;
import com.phonepe.columbus.search.query.term.RangeQuery;
import com.phonepe.columbus.search.query.term.TermQuery;
import com.phonepe.columbus.search.query.term.TermsQuery;
import com.phonepe.columbus.search.query.term.WildcardQuery;
import com.phonepe.columbus.search.query.text.MatchBoolPrefixQuery;
import com.phonepe.columbus.search.query.text.MatchPhrasePrefixQuery;
import com.phonepe.columbus.search.query.text.MatchPhraseQuery;
import com.phonepe.columbus.search.query.text.MatchQuery;
import com.phonepe.columbus.search.query.text.MultiMatchQuery;
import lombok.NonNull;

/**
 * A query represents a certain criteria to select a subset of data. This can be a simple leaf query, or a complex
 * nested query.
 */
@JsonClassDescription("Base interface for all query types. Queries define search criteria to filter and retrieve documents from the search index.")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
@JsonSubTypes({
        // Compound queries
        @JsonSubTypes.Type(BoolQuery.class),
        @JsonSubTypes.Type(BoostingQuery.class),
        @JsonSubTypes.Type(ConstantScoreQuery.class),
        @JsonSubTypes.Type(DisMaxQuery.class),
        @JsonSubTypes.Type(FunctionScoreQuery.class),
        // Geo queries
        @JsonSubTypes.Type(GeoPointQuery.class),
        @JsonSubTypes.Type(GeoDistanceQuery.class),
        // Simple queries
        @JsonSubTypes.Type(MatchAllQuery.class),
        @JsonSubTypes.Type(MatchNoneQuery.class),
        // Special queries
        @JsonSubTypes.Type(ScriptQuery.class),
        @JsonSubTypes.Type(KnnQuery.class),
        // Term queries
        @JsonSubTypes.Type(FieldExistsQuery.class),
        @JsonSubTypes.Type(FuzzyQuery.class),
        @JsonSubTypes.Type(IdsQuery.class),
        @JsonSubTypes.Type(PrefixQuery.class),
        @JsonSubTypes.Type(RangeQuery.class),
        @JsonSubTypes.Type(TermQuery.class),
        @JsonSubTypes.Type(TermsQuery.class),
        @JsonSubTypes.Type(WildcardQuery.class),
        // Text queries
        @JsonSubTypes.Type(MatchBoolPrefixQuery.class),
        @JsonSubTypes.Type(MatchPhrasePrefixQuery.class),
        @JsonSubTypes.Type(MatchPhraseQuery.class),
        @JsonSubTypes.Type(MatchQuery.class),
        @JsonSubTypes.Type(MultiMatchQuery.class),

        // Predicate filters
        @JsonSubTypes.Type(AndFilter.class),
        @JsonSubTypes.Type(OrFilter.class),
        @JsonSubTypes.Type(NotFilter.class),
        // General filters
        @JsonSubTypes.Type(EqualsFilter.class),
        @JsonSubTypes.Type(InFilter.class),
        // Range filters
        @JsonSubTypes.Type(GreaterThanFilter.class),
        @JsonSubTypes.Type(GreaterThanEqualsFilter.class),
        @JsonSubTypes.Type(LessThanFilter.class),
        @JsonSubTypes.Type(LessThanEqualsFilter.class),
        @JsonSubTypes.Type(BetweenFilter.class),
        // String filters
        @JsonSubTypes.Type(TextMatchFilter.class),
        // Score filters
        @JsonSubTypes.Type(FixedScoreFilter.class)
})
public interface Query {

    @NonNull
    @JsonPropertyDescription("The type identifier for this query, which is mandatory")
    String getType();
}
