package com.phonepe.platform.columbus.elastic.storage.handlers;

import com.phonepe.platform.columbus.elastic.internal.storage.ColumbusElasticClient;
import com.phonepe.platform.columbus.storage.commands.VersionCommand;
import com.phonepe.platform.columbus.test.extensions.WithMockito;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WithMockito
class VersionHandlerTest {

    private static final VersionCommand VERSION = VersionCommand.builder().build();

    @Nested
    class FailureTest {

        @Mock(answer = Answers.RETURNS_DEEP_STUBS)
        private ColumbusElasticClient storage;

        @Test
        void testCommand() throws IOException {
            when(storage.client().info(any())).thenThrow(IOException.class);

            final var handler = new VersionHandler<ColumbusElasticClient>();

            assertThrows(IOException.class, () -> handler.handle(VERSION, storage),
                    "error must be propagated");
        }
    }
}
