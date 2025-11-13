package com.phonepe.commons.columbus.elastic9.common;

import lombok.experimental.UtilityClass;

/**
 * A collection of constant values used within the module.
 */
@UtilityClass
@SuppressWarnings({"java:S116", "java:S1170"}) // Sonar does not identify Lombok's utility class
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
        public final String CREATE_INDEX = "createIndex";
        public final String INDEX_HEALTH_CHECK = "indexHealthCheck";
        public final String INDEX_EXISTS = "indexExists";
        public final String GET_INDEX = "getIndex";
        public final String DELETE_INDEX = "deleteIndex";
        public final String PUT_MAPPING = "putMapping";
        public final String UPDATE_SETTINGS = "updateSettings";
        public final String CREATE_INDEX_TEMPLATE = "createIndexTemplate";
        public final String DELETE_INDEX_TEMPLATE = "deleteIndexTemplate";
        public final String GET_DOC = "getDoc";
        public final String INDEX_DOC = "indexDoc";
        public final String UPSERT_DOC = "upsertDoc";
        public final String DELETE_DOC = "deleteDoc";
        public final String BULK_GET_DOC = "bulkGetDoc";
        public final String BULK_INDEX_DOC = "bulkIndexDoc";
        public final String BULK_UPSERT_DOC = "bulkUpsertDoc";
        public final String BULK_DELETE_DOC = "bulkDeleteDoc";
        public final String SEARCH_DOCS = "searchDocs";
        public final String SCROLL_DOCS = "scrollDocs";
        public final String COUNT_DOCS = "countDocs";
        public final String REINDEX_DOCS = "reindexDocs";
        public final String REINDEX_ASYNC = "reindexAsync";
        public final String REINDEX_STATUS = "reindexStatus";
    }

    /**
     * A collection of different field names and properties used by ElasticSearch.
     */
    @UtilityClass
    public class Properties {

        public static final String SHARD_COUNT = "number_of_shards";
        public static final String REPLICA_COUNT = "number_of_replicas";
    }
}
