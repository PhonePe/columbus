package com.phonepe.columbus.elastic7.internal.storage.handlers.search;

import com.phonepe.columbus.elastic7.common.Constants.Operations;
import com.phonepe.columbus.elastic7.dao.node.ElasticNode;
import com.phonepe.columbus.elastic7.internal.storage.utils.ElasticClientUtils;
import com.phonepe.columbus.elastic7.storage.ElasticClient;
import com.phonepe.columbus.elastic7.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.misc.result.TotalCount;
import com.phonepe.columbus.storage.commands.search.CountCommand;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;

import java.io.IOException;
import java.util.Optional;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

/**
 * Count entities in an ElasticSearch index.
 *
 * @param <E> the type of the Elastic client.
 * @implNote the {@link CountCommand#getThreshold()} is applied at a shard level to terminate counting early.
 */
@Slf4j
@RegistryKey(name = CountCommand.COMMAND_NAME, type = CountCommand.class)
public class CountHandler<E extends ElasticClient<E>>
        implements ElasticStorageCommandHandler<TotalCount, CountCommand, E> {

    @Override
    @SneakyThrows
    public TotalCount handle(final CountCommand command, final E storage) {
        final var request = buildRequest(command, storage);

        final CountResponse response;
        try {
            response = count(storage, request);
        } catch (IOException e) {
            log.error("Failed to execute count: {} with error", request, e);
            throw e;
        }

        return parseResponse(response);
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.COUNT_DOCS)
    private CountResponse count(final E storage, final CountRequest request) throws IOException {
        return storage.client().count(request, RequestOptions.DEFAULT);
    }

    private CountRequest buildRequest(final CountCommand command, final E storage) {
        final var toolkit = storage.toolkit();
        final var indices = command.getIndices().stream()
                .map(index -> ElasticClientUtils.resolveIndexName(toolkit, index))
                .toArray(String[]::new);

        final var request = new CountRequest(indices);

        final var node = Optional.ofNullable(command.getNodeId())
                .map(toolkit.nodeIdResolver()::resolve);
        node.map(ElasticNode::getRouting)
                .filter(CollectionUtils::isNotEmpty)
                .map(routing -> routing.toArray(String[]::new))
                .ifPresent(request::routing);
        node.map(ElasticNode::getPreference).ifPresent(request::preference);

        applyIfNotNull(command.getQuery(), toolkit.queryTransformer()::transform, request::query);
        applyIfNotNull(command.getThreshold(), Math::toIntExact, request::terminateAfter);

        return request;
    }

    private TotalCount parseResponse(final CountResponse response) {
        return Boolean.TRUE.equals(response.isTerminatedEarly())
                ? TotalCount.atLeast(response.getCount())
                : TotalCount.exact(response.getCount());
    }
}
