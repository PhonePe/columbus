package com.phonepe.platform.columbus.elastic.extensions;

import com.phonepe.platform.columbus.elastic.storage.configuration.auth.Credentials;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestConstants {

    public final String ELASTIC_SEARCH_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch";
    public final String ELASTIC_SEARCH_VERSION = "7.10.2";
    public final int ELASTIC_SEARCH_PORT = 9200;

    public final Credentials ELASTIC_SEARCH_CREDENTIALS = Credentials.builder()
            .username("elastic")
            .password("password")
            .build();
}
