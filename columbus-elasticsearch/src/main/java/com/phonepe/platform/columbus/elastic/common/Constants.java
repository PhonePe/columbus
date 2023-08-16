package com.phonepe.platform.columbus.elastic.common;

import lombok.experimental.UtilityClass;

/**
 * A collection of constant values used within the module.
 */
@UtilityClass
public class Constants {

    public final String NAME = "ELASTIC";

    /**
     * A collection of names of different operations performed on the elastic client.
     */
    @UtilityClass
    public class Operations {

        public final String CLIENT_NAME = "ElasticClient";

        public final String VERSION = "version";
        public final String HEALTH_CHECK = "healthCheck";
    }
}
