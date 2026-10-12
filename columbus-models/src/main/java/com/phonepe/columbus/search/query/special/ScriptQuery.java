package com.phonepe.columbus.search.query.special;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.search.misc.script.Script;
import com.phonepe.columbus.search.query.AbstractQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Query that uses a custom script to determine which documents match")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(ScriptQuery.TYPE)
public class ScriptQuery extends AbstractQuery {

    public static final String TYPE = "SCRIPT";

    @JsonPropertyDescription("Script definition that returns a boolean indicating whether a document matches")
    private Script script;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
