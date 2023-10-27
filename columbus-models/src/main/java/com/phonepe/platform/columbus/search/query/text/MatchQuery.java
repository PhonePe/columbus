package com.phonepe.platform.columbus.search.query.text;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.search.misc.text.Analysis;
import com.phonepe.platform.columbus.search.misc.text.Fuzziness;
import com.phonepe.platform.columbus.search.misc.text.Terms;
import com.phonepe.platform.columbus.search.query.FieldQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(MatchQuery.TYPE)
public class MatchQuery extends FieldQuery {

    public static final String TYPE = "MATCH";

    private Object value;

    private Analysis analysis;
    private Terms terms;
    private Fuzziness fuzziness;

    private Boolean ignoreErrors;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
