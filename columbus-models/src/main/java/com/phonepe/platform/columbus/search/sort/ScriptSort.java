package com.phonepe.platform.columbus.search.sort;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.search.misc.script.Script;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(ScriptSort.TYPE)
public class ScriptSort extends BaseSort {

    public static final String TYPE = "SCRIPT";

    private Script script;
    private String valueType;

    private String sortMode;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
