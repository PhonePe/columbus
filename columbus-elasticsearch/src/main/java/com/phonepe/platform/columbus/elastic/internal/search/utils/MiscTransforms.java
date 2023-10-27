package com.phonepe.platform.columbus.elastic.internal.search.utils;

import com.phonepe.platform.columbus.common.Elements;
import com.phonepe.platform.columbus.dao.id.Ids;
import com.phonepe.platform.columbus.search.misc.bucket.BucketSortOrder;
import com.phonepe.platform.columbus.search.misc.bucket.NamedRange;
import com.phonepe.platform.columbus.search.misc.geo.Location;
import com.phonepe.platform.columbus.search.misc.query.Projection;
import com.phonepe.platform.columbus.search.misc.result.Hit;
import com.phonepe.platform.columbus.search.misc.result.Result;
import com.phonepe.platform.columbus.search.misc.result.TotalCount;
import com.phonepe.platform.columbus.search.sort.SortOrder;
import com.phonepe.platform.columbus.serde.SerDe;
import com.phonepe.platform.columbus.type.EntityType;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregator.Range;
import org.elasticsearch.search.aggregations.bucket.terms.IncludeExclude;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;

import java.util.Arrays;
import java.util.List;

/**
 * A collection of utilities to transform misc models to and from ElasticSearch models.
 */
@Slf4j
@UtilityClass
public class MiscTransforms {

    public IncludeExclude toElastic(
            final com.phonepe.platform.columbus.search.misc.bucket.IncludeExclude includeExclude
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

    public Script toElastic(final com.phonepe.platform.columbus.search.misc.script.Script script) {
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

    public <T> Result<T> fromElastic(final SearchHits hits, final EntityType<T> type, final SerDe serDe) {
        final var builder = Result.<T>builder()
                .totalCount(fromElastic(hits.getTotalHits()))
                .maxScore(hits.getMaxScore());
        Arrays.stream(hits.getHits()).map(hit -> fromElastic(hit, type, serDe))
                .forEachOrdered(builder::hit);
        return builder.build();
    }

    public <T> Hit<T> fromElastic(final SearchHit hit, final EntityType<T> type, final SerDe serDe) {
        return Hit.<T>builder()
                .id(Ids.value(hit.getId()))
                .entity(serDe.fromString(hit.getSourceAsString(), type))
                .score(hit.getScore())
                .matchedQueries(List.of(hit.getMatchedQueries()))
                .sortValues(List.of(hit.getSortValues()))
                .build();
    }

    public TotalCount fromElastic(final TotalHits totalHits) {
        return switch (totalHits.relation) {
            case EQUAL_TO -> TotalCount.exact(totalHits.value);
            case GREATER_THAN_OR_EQUAL_TO -> TotalCount.atLeast(totalHits.value);
        };
    }
}
