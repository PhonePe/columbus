package com.phonepe.commons.columbus.search.misc.text;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Fuzziness {

    /**
     * The edit distance allowed. Could be configured separately for different lengths, if the storage supports.
     */
    private String amount;

    /**
     * Length of prefix to be matched exactly.
     */
    private Integer prefixLength;

    /**
     * Indicates if edits include transpositions of adjacent characters.
     */
    private Boolean transposition;

    /**
     * The algorithm used to rewrite the fuzzy query.
     */
    private String rewrite;
}
