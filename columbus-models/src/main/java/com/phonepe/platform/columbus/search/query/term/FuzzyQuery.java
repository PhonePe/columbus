package com.phonepe.platform.columbus.search.query.term;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.search.misc.text.Fuzziness;
import com.phonepe.platform.columbus.search.query.FieldQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(FuzzyQuery.TYPE)
public class FuzzyQuery extends FieldQuery {

    public static final String TYPE = "FUZZY";

    private Object value;

    private Fuzziness fuzziness;

    private Integer maximumExpansions;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
