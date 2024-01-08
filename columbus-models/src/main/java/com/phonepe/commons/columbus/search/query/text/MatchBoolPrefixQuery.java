package com.phonepe.commons.columbus.search.query.text;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.misc.text.Analysis;
import com.phonepe.commons.columbus.search.misc.text.Fuzziness;
import com.phonepe.commons.columbus.search.misc.text.Terms;
import com.phonepe.commons.columbus.search.query.FieldQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(MatchBoolPrefixQuery.TYPE)
public class MatchBoolPrefixQuery extends FieldQuery {

    public static final String TYPE = "MATCH_BOOL_PREFIX";

    private Object value;

    private Analysis analysis;
    private Terms terms;
    private Fuzziness fuzziness;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
