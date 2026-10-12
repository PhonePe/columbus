package com.phonepe.columbus.elastic9.internal.storage.handlers.search;

import co.elastic.clients.elasticsearch.core.SearchRequest;
import com.phonepe.columbus.elastic9.dao.node.ElasticNode;
import com.phonepe.columbus.elastic9.internal.search.utils.ElasticScanner;
import com.phonepe.columbus.elastic9.internal.search.utils.MiscTransforms;
import com.phonepe.columbus.elastic9.internal.search.utils.SearchEnums;
import com.phonepe.columbus.elastic9.internal.storage.utils.ElasticClientUtils;
import com.phonepe.columbus.elastic9.storage.ElasticClient;
import com.phonepe.columbus.elastic9.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.commands.search.ScanCommand;
import com.phonepe.columbus.storage.commands.search.ScanCommand.Scanner;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Optional;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

/**
 * Scan entities in an ElasticSearch index.
 *
 * @param <E> the type of the Elastic client.
 */
@Slf4j
@RegistryKey(name = ScanCommand.COMMAND_NAME, type = ScanCommand.class)
public class ScanHandler<T, E extends ElasticClient<E>>
        implements ElasticStorageCommandHandler<Scanner<T>, ScanCommand<T>, E> {

    @Override
    @SneakyThrows
    public Scanner<T> handle(final ScanCommand<T> command, final E storage) {
        return new ElasticScanner<>(buildRequest(command, storage), command, storage);
    }

    private SearchRequest buildRequest(final ScanCommand<T> command, final E storage) {
        if (command.getIndices().isEmpty()) {
            throw new IllegalArgumentException("Scan requires at least one index");
        }
        final var toolkit = storage.toolkit();
        final var indices = command.getIndices().stream()
                .map(index -> ElasticClientUtils.resolveIndexName(toolkit, index))
                .toList();

        final var request = new SearchRequest.Builder().index(indices)
                .scroll(SearchEnums.toTime(command.getDurationInSeconds()))
                .size(command.getBatchSize());

        applyIfNotNull(command.getQuery(), toolkit.queryTransformer()::transform, request::query);

        command.getSorts().stream()
                .map(toolkit.sortTransformer()::transform)
                .forEachOrdered(request::sort);

        applyIfNotNull(command.getProjection(), MiscTransforms::toElastic, request::source);

        final var node = Optional.ofNullable(command.getNodeId())
                .map(toolkit.nodeIdResolver()::resolve);
        node.map(ElasticNode::getRouting)
                .filter(CollectionUtils::isNotEmpty)
                .map(set -> String.join(",", set))
                .ifPresent(request::routing);
        node.map(ElasticNode::getPreference).ifPresent(request::preference);

        return request.build();
    }
}
