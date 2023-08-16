package com.phonepe.platform.columbus.elastic.internal.storage.lifecycle;

import com.phonepe.platform.columbus.test.extensions.WithMockito;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@WithMockito
class ElasticClientManagedTest {

    @Mock
    private RestHighLevelClient client;
    private ElasticClientManaged managed;

    @BeforeEach
    void setUp() {
        managed = new ElasticClientManaged(client);
    }

    @Test
    void testStart() {
        managed.start();

        verifyNoInteractions(client);
    }

    @Test
    void testStop() throws Exception {
        managed.stop();

        verify(client, times(1)).close();
        verifyNoMoreInteractions(client);
    }
}
