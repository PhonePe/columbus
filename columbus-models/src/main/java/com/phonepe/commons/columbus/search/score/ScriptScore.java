package com.phonepe.commons.columbus.search.score;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.misc.script.Script;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(ScriptScore.TYPE)
public class ScriptScore extends AbstractScoreFunction {

    public static final String TYPE = "SCRIPT";

    private Script script;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
