package com.phonepe.platform.columbus.core.search.preprocessor;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.phonepe.platform.columbus.models.core.search.value.ReferenceMap;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type"
)
public interface Preprocessor {
    String getType();

    void process(ReferenceMap references);

    static Preprocessor noOp() {
        return new NoOpPreprocessor();
    }
}
