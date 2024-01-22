package com.phonepe.commons.columbus.search.query.special;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.misc.script.Script;
import com.phonepe.commons.columbus.search.query.AbstractQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(ScriptQuery.TYPE)
public class ScriptQuery extends AbstractQuery {

    public static final String TYPE = "SCRIPT";

    private Script script;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
