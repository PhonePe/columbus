package com.phonepe.platform.columbus.core.search.preprocessor;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.models.core.search.value.ReferenceMap;

@JsonTypeName(NoOpPreprocessor.TYPE)
public class NoOpPreprocessor implements Preprocessor {
    public static final String TYPE = "NO_OP";

    @Override
    public String getType() {
        return NoOpPreprocessor.TYPE;
    }

    @Override
    public void process(ReferenceMap references) {
        // do nothing
    }
}
