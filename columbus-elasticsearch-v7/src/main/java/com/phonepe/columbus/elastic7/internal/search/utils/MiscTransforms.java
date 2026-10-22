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
package com.phonepe.columbus.elastic7.internal.search.utils;

import com.phonepe.columbus.common.Elements;
import com.phonepe.columbus.common.pair.Pair;
import com.phonepe.columbus.common.pair.PairFunctions;
import com.phonepe.columbus.dao.id.Ids;
import com.phonepe.columbus.elastic7.storage.toolkit.ElasticToolkit;
import com.phonepe.columbus.search.misc.bucket.BucketSortOrder;
import com.phonepe.columbus.search.misc.bucket.NamedRange;
import com.phonepe.columbus.search.misc.geo.Location;
import com.phonepe.columbus.search.misc.query.Collapse;
import com.phonepe.columbus.search.misc.query.InnerQuery;
import com.phonepe.columbus.search.misc.query.Projection;
import com.phonepe.columbus.search.misc.result.Hit;
import com.phonepe.columbus.search.misc.result.Result;
import com.phonepe.columbus.search.misc.result.TotalCount;
import com.phonepe.columbus.search.sort.SortOrder;
import com.phonepe.columbus.serde.SerDe;
import com.phonepe.columbus.type.EntityType;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.geometry.Geometry;
import org.elasticsearch.geometry.Point;
import org.elasticsearch.index.query.InnerHitBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregator.Range;
import org.elasticsearch.search.aggregations.bucket.terms.IncludeExclude;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.SortBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

/**
 * A collection of utilities to transform misc models to and from ElasticSearch models.
 */
@Slf4j
@UtilityClass
public class MiscTransforms {

    public IncludeExclude toElastic(
            final com.phonepe.columbus.search.misc.bucket.IncludeExclude includeExclude
    ) {
        final var include = includeExclude.getInclude();
        final var exclude = includeExclude.getExclude();
        if (includeExclude.isRegex()) {
            if (include.size() > 1) {
                log.warn("Regex include-exclude only supports 1 expression for inclusion, got {}", include.size());
            }
            if (exclude.size() > 1) {
                log.warn("Regex include-exclude only supports 1 expression for exclusion, got {}", exclude.size());
            }
            return new IncludeExclude(Elements.firstOrNull(include), Elements.firstOrNull(exclude));
        }
        return new IncludeExclude(include.toArray(String[]::new), exclude.toArray(String[]::new));
    }

    public BucketOrder toElastic(final BucketSortOrder bucketSortOrder) {
        final var ascending = bucketSortOrder.getOrder() == null || SortOrder.ASC.equals(bucketSortOrder.getOrder());
        return switch (bucketSortOrder.getName()) {
            case BucketSortOrder.COUNT -> BucketOrder.count(ascending);
            case BucketSortOrder.KEY -> BucketOrder.key(ascending);
            default -> BucketOrder.aggregation(bucketSortOrder.getName(), ascending);
        };
    }

    public Range toElastic(final NamedRange range) {
        return new Range(range.getName(), range.getFrom(), range.getTo());
    }

    public FetchSourceContext toElastic(final Projection projection) {
        return new FetchSourceContext(
                !projection.disabled(),
                projection.getIncludeFields().toArray(String[]::new),
                projection.getExcludeFields().toArray(String[]::new));
    }

    public CollapseBuilder toElastic(final Collapse collapse, final ElasticToolkit toolkit) {
        final var builder = new CollapseBuilder(collapse.getField())
                .setInnerHits(collapse.getInnerQueries().stream()
                        .map(innerQuery -> toElastic(innerQuery, toolkit))
                        .toList());
        applyIfNotNull(collapse.getParallelism(), builder::setMaxConcurrentGroupRequests);
        return builder;
    }

    public <T> InnerHitBuilder toElastic(final InnerQuery<T> query, final ElasticToolkit toolkit) {
        final var builder = new InnerHitBuilder()
                .setName(query.getName())
                .setFrom(query.getFrom())
                .setSize(query.getSize())
                .setSorts(query.getSorts().stream()
                        .<SortBuilder<?>>map(toolkit.sortTransformer()::transform)
                        .toList());

        applyIfNotNull(query.getIgnoreInvalid(), builder::setIgnoreUnmapped);
        applyIfNotNull(query.getProjection(), MiscTransforms::toElastic, builder::setFetchSourceContext);
        applyIfNotNull(query.getInnerCollapse(), collapse -> toElastic(collapse, toolkit), builder::setInnerCollapse);
        return builder;
    }

    public Script toElastic(final com.phonepe.columbus.search.misc.script.Script script) {
        if (script.getId() != null) {
            return new Script(ScriptType.STORED, null, script.getId(), script.getParameters());
        }
        final var language = ObjectUtils.defaultIfNull(script.getLanguage(), Script.DEFAULT_SCRIPT_LANG);
        return new Script(ScriptType.INLINE, language, script.getSource(), script.getParameters());
    }

    public Fuzziness toElasticFuzziness(final String amount) {
        return Fuzziness.build(amount);
    }

    public GeoPoint toElasticGeoPoint(final Location center) {
        return new GeoPoint(center.getLat(), center.getLon());
    }

    public Geometry toElasticGeometry(final Location point) {
        return new Point(point.getLon(), point.getLat());
    }

    public <T> Result<T> fromElastic(final SearchHits hits, final EntityType<T> type, final SerDe serDe) {
        return fromElastic(hits, type, List.of(), serDe);
    }

    public <T> Result<T> fromElastic(
            final SearchHits hits,
            final EntityType<T> type,
            final List<InnerQuery<?>> innerQueries,
            final SerDe serDe
    ) {
        final var builder = Result.<T>builder()
                .totalCount(fromElastic(hits.getTotalHits()))
                .maxScore(hits.getMaxScore());
        Arrays.stream(hits.getHits()).map(hit -> fromElastic(hit, type, innerQueries, serDe))
                .forEachOrdered(builder::hit);
        return builder.build();
    }

    public <T> Result<T> fromElastic(
            final SearchHits hits,
            final EntityType<T> type,
            final SerDe serDe,
            @Nullable final Collapse collapse
    ) {
        return fromElastic(hits, type, collapse == null ? List.of() : collapse.getInnerQueries(), serDe);
    }

    public <T> Hit<T> fromElastic(
            final SearchHit hit,
            final EntityType<T> type,
            final List<InnerQuery<?>> innerQueries,
            final SerDe serDe
    ) {
        final var builder = Hit.<T>builder()
                .id(Ids.value(hit.getId()))
                .entity(serDe.fromString(hit.getSourceAsString(), type))
                .score(hit.getScore())
                .matchedQueries(List.of(hit.getMatchedQueries()))
                .sortValues(List.of(hit.getSortValues()));

        final var innerHits = MapUtils.emptyIfNull(hit.getInnerHits());
        innerQueries.stream()
                .map(innerQuery -> Pair.of(innerQuery, innerHits.get(innerQuery.getName())))
                .filter(PairFunctions.filteringSecond(Objects::nonNull))
                .forEach(PairFunctions.consume((innerQuery, innerHit) -> builder.innerResult(
                        innerQuery.getName(),
                        fromElastic(innerHit, innerQuery.getEntityType(), serDe, innerQuery.getInnerCollapse()))));

        return builder.build();
    }

    public TotalCount fromElastic(final TotalHits totalHits) {
        return switch (totalHits.relation) {
            case EQUAL_TO -> TotalCount.exact(totalHits.value);
            case GREATER_THAN_OR_EQUAL_TO -> TotalCount.atLeast(totalHits.value);
        };
    }
}
