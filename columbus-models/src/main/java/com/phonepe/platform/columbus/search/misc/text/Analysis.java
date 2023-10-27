package com.phonepe.platform.columbus.search.misc.text;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * Defines the configuration for analysing text queries.
 */
@Data
@Builder
@Jacksonized
public class Analysis {

    /**
     * The analyzer algorithm to use.
     */
    private String analyzer;

    /**
     * Maximum number of terms the query will expand to.
     */
    private Integer maximumExpansions;

    /**
     * Indicates if all documents must be returned if the query results in zero terms.
     */
    private Boolean returnAllForZeroTerms;

    /**
     * Indicates if the synonyms for the terms should be used in the final query.
     */
    private Boolean generateSynonymQueries;
}
