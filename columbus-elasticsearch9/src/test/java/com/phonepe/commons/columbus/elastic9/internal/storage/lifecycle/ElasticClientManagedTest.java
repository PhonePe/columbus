package com.phonepe.commons.columbus.elastic9.internal.storage.lifecycle;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class ElasticClientManagedTest {

    @Mock
    private ElasticsearchClient client;
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
