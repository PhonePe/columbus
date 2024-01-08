package com.phonepe.commons.columbus.search.misc.text;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Terms {

    /**
     * Indicates if all the (generated) terms must match or the minimum specified by {@link #minimumMatch}.
     */
    private Boolean matchAllTerms;

    /**
     * The minimum number of terms that must match for each document. Default behaviour effectively matches at least
     * one.
     */
    private String minimumMatch;
}
