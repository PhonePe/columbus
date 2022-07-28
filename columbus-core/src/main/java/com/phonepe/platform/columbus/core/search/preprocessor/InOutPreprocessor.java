package com.phonepe.platform.columbus.core.search.preprocessor;

import com.phonepe.platform.columbus.core.search.value.ValueExtractor;
import com.phonepe.platform.columbus.models.core.search.value.ReferenceMap;
import com.phonepe.platform.columbus.models.core.search.value.Value;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class InOutPreprocessor<I, O> implements Preprocessor {
    private Value<I> input;
    private String outputName;

    @Override
    public void process(ReferenceMap references) {
        references.put(outputName, preprocess(input.accept(new ValueExtractor<>(references))));
    }

    protected abstract O preprocess(I in);
}
