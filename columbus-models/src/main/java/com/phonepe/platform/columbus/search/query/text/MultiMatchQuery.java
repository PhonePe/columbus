package com.phonepe.platform.columbus.search.query.text;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.search.misc.text.Analysis;
import com.phonepe.platform.columbus.search.misc.text.Fuzziness;
import com.phonepe.platform.columbus.search.misc.text.Terms;
import com.phonepe.platform.columbus.search.query.BaseQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.Collection;
import java.util.Map;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(MultiMatchQuery.TYPE)
public class MultiMatchQuery extends BaseQuery {

    public static final String TYPE = "MULTI_MATCH";

    private String matchType;

    private Object value;

    @Singular(ignoreNullCollections = true)
    private Map<@NonNull String, @NonNull Float> fieldBoosts;
    private Float tieBreaker;

    private Analysis analysis;
    private Terms terms;
    private Fuzziness fuzziness;

    private Integer slop;

    private Boolean ignoreErrors;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }

    @SuppressWarnings("unused")
    public abstract static class MultiMatchQueryBuilder<C extends MultiMatchQuery,
            B extends MultiMatchQueryBuilder<C, B>> extends BaseQueryBuilder<C, B> {

        public B fieldName(final String fieldName) {
            return fieldName == null ? self() : fieldBoost(fieldName, 1.0f);
        }

        public B fieldNames(final Collection<String> fieldNames) {
            if (fieldNames != null) {
                fieldNames.forEach(this::fieldName);
            }
            return self();
        }
    }
}
