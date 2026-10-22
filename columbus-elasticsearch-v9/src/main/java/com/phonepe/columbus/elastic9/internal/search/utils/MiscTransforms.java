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
package com.phonepe.columbus.elastic9.internal.search.utils;

import co.elastic.clients.elasticsearch._types.ErrorCause;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.GeoLocation;
import co.elastic.clients.elasticsearch._types.KnnSearch;
import co.elastic.clients.elasticsearch._types.LatLonGeoLocation;
import co.elastic.clients.elasticsearch._types.RescoreVector;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.ScriptSource;
import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationRange;
import co.elastic.clients.elasticsearch._types.aggregations.TermsExclude;
import co.elastic.clients.elasticsearch._types.aggregations.TermsInclude;
import co.elastic.clients.elasticsearch._types.query_dsl.GeoShapeFieldQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.FieldCollapse;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.core.search.InnerHits;
import co.elastic.clients.elasticsearch.core.search.SourceConfig;
import co.elastic.clients.elasticsearch.core.search.SourceFilter;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.util.NamedValue;
import com.phonepe.columbus.common.Elements;
import com.phonepe.columbus.common.pair.Pair;
import com.phonepe.columbus.common.pair.PairFunctions;
import com.phonepe.columbus.dao.id.Ids;
import com.phonepe.columbus.elastic9.dao.node.ElasticNode;
import com.phonepe.columbus.elastic9.internal.storage.utils.ElasticClientUtils;
import com.phonepe.columbus.elastic9.storage.toolkit.ElasticToolkit;
import com.phonepe.columbus.search.aggregator.Aggregator;
import com.phonepe.columbus.search.misc.bucket.BucketSortOrder;
import com.phonepe.columbus.search.misc.bucket.IncludeExclude;
import com.phonepe.columbus.search.misc.bucket.NamedRange;
import com.phonepe.columbus.search.misc.geo.Location;
import com.phonepe.columbus.search.misc.query.Collapse;
import com.phonepe.columbus.search.misc.query.InnerQuery;
import com.phonepe.columbus.search.misc.query.Projection;
import com.phonepe.columbus.search.misc.result.Hit;
import com.phonepe.columbus.search.misc.result.Result;
import com.phonepe.columbus.search.misc.result.TotalCount;
import com.phonepe.columbus.search.query.special.KnnQuery;
import com.phonepe.columbus.search.sort.SortOrder;
import com.phonepe.columbus.serde.SerDe;
import com.phonepe.columbus.storage.commands.search.SearchBase;
import com.phonepe.columbus.storage.models.result.SearchResult;
import com.phonepe.columbus.type.EntityType;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import javax.annotation.Nullable;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static co.elastic.clients.elasticsearch._types.SortOrder.Asc;
import static co.elastic.clients.elasticsearch._types.SortOrder.Desc;
import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

/**
 * A collection of utilities to transform misc models to and from ElasticSearch models.
 */
@Slf4j
@UtilityClass
public class MiscTransforms {

    public TermsInclude toElasticTermsInclude(final IncludeExclude includeExclude) {
        final var builder = new TermsInclude.Builder();
        final var include = includeExclude.getInclude();
        if (includeExclude.isRegex()) {
            if (include.size() > 1) {
                log.warn("Regex include-exclude only supports 1 expression for inclusion, got {}", include.size());
            }
            return builder.regexp(Elements.firstOrNull(include)).build();
        }
        return builder.terms(include).build();
    }

    public TermsExclude toElasticTermsExclude(final IncludeExclude includeExclude) {
        final var builder = new TermsExclude.Builder();
        final var exclude = includeExclude.getExclude();
        if (includeExclude.isRegex()) {
            if (exclude.size() > 1) {
                log.warn("Regex include-exclude only supports 1 expression for exclusion, got {}", exclude.size());
            }
            return builder.regexp(Elements.firstOrNull(exclude)).build();
        }
        return builder.terms(exclude).build();
    }

    public NamedValue<co.elastic.clients.elasticsearch._types.SortOrder> toElastic(final BucketSortOrder bucketSortOrder) {
        final var ascending = bucketSortOrder.getOrder() == null || SortOrder.ASC.equals(bucketSortOrder.getOrder());
        return NamedValue.of(bucketSortOrder.getName(), ascending ? Asc : Desc);
    }

    public AggregationRange toElastic(final NamedRange range) {
        return new AggregationRange.Builder().key(range.getName()).from(range.getFrom()).to(range.getTo()).build();
    }

    public SourceConfig toElastic(final Projection projection) {
        if (projection.disabled()) {
            return new SourceConfig.Builder().fetch(false).build();
        }
        return new SourceConfig.Builder().filter(new SourceFilter.Builder()
                        .excludeVectors(false)
                        .excludes(projection.getExcludeFields())
                        .includes(projection.getIncludeFields())
                        .build())
                .build();
    }

    public FieldCollapse toElastic(final Collapse collapse, final ElasticToolkit toolkit) {
        final var fieldCollapseBuilder = new FieldCollapse.Builder()
                .field(collapse.getField());

        if (!collapse.getInnerQueries().isEmpty()) {
            fieldCollapseBuilder.innerHits(collapse.getInnerQueries().stream()
                    .map(innerQuery -> toElastic(innerQuery, toolkit))
                    .toList());
        }
        applyIfNotNull(collapse.getParallelism(), fieldCollapseBuilder::maxConcurrentGroupSearches);
        return fieldCollapseBuilder.build();
    }

    public <T> InnerHits toElastic(final InnerQuery<T> query, final ElasticToolkit toolkit) {
        final var builder = new InnerHits.Builder()
                .name(query.getName())
                .from(query.getFrom())
                .size(query.getSize())
                .sort(query.getSorts().stream()
                        .map(toolkit.sortTransformer()::transform)
                        .toList());

        applyIfNotNull(query.getIgnoreInvalid(), builder::ignoreUnmapped);
        applyIfNotNull(query.getProjection(), MiscTransforms::toElastic, builder::source);
        applyIfNotNull(query.getInnerCollapse(), collapse -> toElastic(collapse, toolkit), builder::collapse);
        return builder.build();
    }

    public Script toElastic(final com.phonepe.columbus.search.misc.script.Script script) {
        final var params = script.getParameters().entrySet().stream().collect(Collectors.toMap(
                Entry::getKey,
                entry -> JsonData.of(entry.getValue())));

        if (script.getId() != null) {
            return new Script.Builder()
                    .id(script.getId())
                    .params(params)
                    .build();
        }

        return new Script.Builder()
                .source(new ScriptSource.Builder()
                        .scriptString(script.getSource())
                        .build())
                .id(script.getId())
                .params(params)
                .build();
    }

    public GeoLocation toElasticGeoPoint(final Location center) {
        return new GeoLocation.Builder()
                .latlon(new LatLonGeoLocation.Builder()
                        .lat(center.getLat())
                        .lon(center.getLon())
                        .build())
                .build();
    }

    public GeoShapeFieldQuery toElasticGeometry(final Location point) {
        final StringReader jsonData = new StringReader(String.format("""
                {
                    "type":"Point",
                    "coordinates": [%f, %f]
                }
                """, point.getLon(), point.getLat()));
        return new GeoShapeFieldQuery.Builder()
                .shape(JsonData.from(jsonData))
                .build();
    }

    public <T> SearchRequest.Builder toElastic(final SearchBase<T> command, final ElasticToolkit toolkit) {
        final var searchRequest = new SearchRequest.Builder();

        command.getIndexBoosts().forEach((key, value) -> {
            final var indexName = ElasticClientUtils.resolveIndexName(toolkit, key);
            searchRequest
                    .index(indexName)
                    .indicesBoost(NamedValue.of(indexName, (double) value));
        });

        command.getAggregators().stream()
                .collect(Collectors.toMap(Aggregator::getName, toolkit.aggregatorTransformer()::transform))
                .forEach(searchRequest::aggregations);

        applyIfNotNull(
                command.getCollapse(),
                collapse -> MiscTransforms.toElastic(collapse, toolkit),
                searchRequest::collapse);

        applyIfNotNull(command.getPostFilter(), toolkit.queryTransformer()::transform, searchRequest::postFilter);

        applyIfNotNull(command.getProjection(), MiscTransforms::toElastic, searchRequest::source);

        Optional.ofNullable(command.getPage())
                .map(toolkit.pageResolver()::resolve)
                .ifPresent(page -> {
                    applyIfNotNull(page.getSize(), searchRequest::size);
                    applyIfNotNull(page.getFrom(), searchRequest::from);
                    applyIfNotNull(page.getAfterKeys(),
                            keys -> keys.stream().map(FieldValue::of).forEach(searchRequest::searchAfter));
                });

        final var node = Optional.ofNullable(command.getNodeId())
                .map(toolkit.nodeIdResolver()::resolve);

        node.map(ElasticNode::getRouting)
                .filter(CollectionUtils::isNotEmpty)
                .map(routing -> String.join( ",", routing))
                .ifPresent(searchRequest::routing);
        node.map(ElasticNode::getPreference).ifPresent(searchRequest::preference);

        return searchRequest;
    }

    public <T> Result<T> fromElastic(
            final HitsMetadata<T> hits,
            final List<InnerQuery<?>> innerQueries,
            final SerDe serDe
    ) {
        if (hits == null) {
            return null;
        }

        final var builder = Result.<T>builder()
                .totalCount(fromElastic(hits.total()));

        applyIfNotNull(hits.maxScore(), Double::floatValue, builder::maxScore);
        hits.hits().stream().map(hit -> fromElastic(hit, innerQueries, serDe))
                .forEachOrdered(builder::hit);
        return builder.build();
    }

    public <T> Result<T> fromElastic(
            final HitsMetadata<T> hits,
            final SerDe serDe,
            @Nullable final Collapse collapse
    ) {
        return fromElastic(hits, collapse == null ? List.of() : collapse.getInnerQueries(), serDe);
    }

    public <T> Result<T> fromElasticForInnerResult(
            final HitsMetadata<JsonData> hits,
            final EntityType<T> type,
            final SerDe serDe,
            @Nullable final Collapse collapse
    ) {
        final List<InnerQuery<?>> innerQueries = collapse == null ? List.of() : collapse.getInnerQueries();
        final var builder = Result.<T>builder()
                .totalCount(fromElastic(hits.total()));
        applyIfNotNull(hits.maxScore(), Double::floatValue, builder::maxScore);
        hits.hits().stream().map(hit -> fromElasticForInnerHit(hit, type, innerQueries, serDe))
                .forEachOrdered(builder::hit);
        return builder.build();

    }

    public <T> Hit<T> fromElastic(
            final co.elastic.clients.elasticsearch.core.search.Hit<T> hit,
            final List<InnerQuery<?>> innerQueries,
            final SerDe serDe
    ) {
        final var builder = Hit.<T>builder()
                .id(Ids.value(hit.id()))
                .entity(hit.source())
                .matchedQueries(hit.matchedQueries().keySet())
                .sortValues(hit.sort().stream().map(FieldValue::_get).toList());

        applyIfNotNull(hit.score(), Double::floatValue, builder::score);

        final var innerHits = MapUtils.emptyIfNull(hit.innerHits());
        innerQueries.stream()
                .map(innerQuery -> Pair.of(innerQuery, innerHits.get(innerQuery.getName())))
                .filter(PairFunctions.filteringSecond(Objects::nonNull))
                .forEach(PairFunctions.consume((innerQuery, innerHit) -> builder.innerResult(
                        innerQuery.getName(),
                        fromElasticForInnerResult(innerHit.hits(), innerQuery.getEntityType(), serDe,
                                innerQuery.getInnerCollapse()))));

        return builder.build();
    }

    public <T> Hit<T> fromElasticForInnerHit(
            final co.elastic.clients.elasticsearch.core.search.Hit<JsonData> hit,
            final EntityType<T> type,
            final List<InnerQuery<?>> innerQueries,
            final SerDe serDe
    ) {
        final var builder = Hit.<T>builder()
                .id(Ids.value(hit.id()))
                .entity(serDe.fromString(hit.source().toJson().toString(), type))
                .matchedQueries(hit.matchedQueries().keySet())
                .sortValues(hit.sort().stream().map(FieldValue::_get).toList());

        applyIfNotNull(hit.score(), Double::floatValue, builder::score);
        final var innerHits = MapUtils.emptyIfNull(hit.innerHits());
        innerQueries.stream()
                .map(innerQuery -> Pair.of(innerQuery, innerHits.get(innerQuery.getName())))
                .filter(PairFunctions.filteringSecond(Objects::nonNull))
                .forEach(PairFunctions.consume((innerQuery, innerHit) -> builder.innerResult(
                        innerQuery.getName(),
                        fromElasticForInnerResult(innerHit.hits(), innerQuery.getEntityType(), serDe, innerQuery.getInnerCollapse()))));

        return builder.build();
    }

    public TotalCount fromElastic(final TotalHits totalHits) {
        if (totalHits == null) {
            return null;
        }

        return switch (totalHits.relation()) {
            case Eq -> TotalCount.exact(totalHits.value());
            case Gte -> TotalCount.atLeast(totalHits.value());
        };
    }

    public String fromElastic(final ErrorCause error) {
        final StringBuilder stringBuilder = new StringBuilder();

        if (error == null) {
            return stringBuilder.toString();
        }

        if (error.type() != null) {
            stringBuilder.append("Type: ").append(error.type()).append(";");
        }

        if (error.reason() != null) {
            stringBuilder.append("Reason: ").append(error.reason()).append(";");
        }

        if (error.stackTrace() != null) {
            stringBuilder.append("Stacktrace: ");
        }

        return stringBuilder.toString();
    }

    public KnnSearch toElastic(final KnnQuery knn, final ElasticToolkit toolkit) {
        final var builder = new KnnSearch.Builder()
                .field(knn.getField())
                .queryVector(knn.getQueryVector());

        applyIfNotNull(knn.getK(), builder::k);
        applyIfNotNull(knn.getNumCandidates(), builder::numCandidates);
        applyIfNotNull(knn.getBoost(), builder::boost);
        applyIfNotNull(knn.getSimilarity(), builder::similarity);
        applyIfNotNull(knn.getOversample(), MiscTransforms::toElasticRescoreVector, builder::rescoreVector);

        if (knn.getFilterClauses() != null) {
            knn.getFilterClauses().stream()
                    .map(toolkit.queryTransformer()::transform)
                    .filter(Objects::nonNull)
                    .forEach(builder::filter);
        }

        return builder.build();
    }

    public RescoreVector toElasticRescoreVector(final Float oversample) {
        return new RescoreVector.Builder().oversample(oversample).build();
    }

    public Time toElasticTime(final Object time) {
        if (time instanceof final String stringValue) {
            return new Time.Builder().time(stringValue).build();
        } else if (time instanceof final Number numberValue) {
            return new Time.Builder().offset(numberValue.intValue()).build();
        } else {
            throw new IllegalArgumentException("Unsupported time value: " + time);
        }
    }

    public GeoLocation toElasticOrigin(final Object location) {
        if (location instanceof String locationString) {
            final List<Double> parts = Arrays.stream(locationString.split(","))
                    .map(String::strip)
                    .map(Double::parseDouble).toList();
            if (parts.size() != 2) {
                throw new IllegalArgumentException(String.format("Unsupported location value: %s. Location should be comma "
                        + "delimited lat long", location));
            }
            return new GeoLocation.Builder().latlon(new LatLonGeoLocation.Builder()
                    .lat(parts.get(0))
                    .lon(parts.get(1))
                    .build())
                    .build();
        } else if (location instanceof Location locationObj) {
            return toElasticGeoPoint(locationObj);
        } else {
            throw new IllegalArgumentException(String.format("Unsupported location value: %s. Location should be comma "
                    + "delimited lat long or Location object", location));
        }
    }

    public <T> SearchResult<T> fromElastic(final SearchResponse<T> response, final SearchBase<T> command,
                                           final ElasticToolkit toolkit) {
        final var builder = SearchResult.<T>builder()
                .result(MiscTransforms.fromElastic(
                        response.hits(),
                        toolkit.serDe(),
                        command.getCollapse()));

        if (response.aggregations() != null) {
            final var parsers = command.getAggregators().stream()
                    .collect(Collectors.toMap(Aggregator::getName, toolkit.aggregationParserResolver()::resolve));

            final Map<String, Aggregate> aggregates = response.aggregations();
            aggregates.forEach((key, aggregate) ->
                    builder.aggregation(key, parsers.get(key).parse(aggregate, key)));
        }

        return builder.build();

    }
}
