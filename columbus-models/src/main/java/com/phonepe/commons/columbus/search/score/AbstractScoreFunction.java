package com.phonepe.commons.columbus.search.score;

import com.phonepe.commons.columbus.search.query.Query;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public abstract class AbstractScoreFunction implements ScoreFunction {

    private Float weight;

    private Query query;
}
