package com.phonepe.columbus.elastic7.internal.storage.handlers.search;

import com.phonepe.columbus.elastic7.dao.node.ElasticNode;
import com.phonepe.columbus.elastic7.internal.search.utils.ElasticScanner;
import com.phonepe.columbus.elastic7.internal.search.utils.MiscTransforms;
import com.phonepe.columbus.elastic7.internal.storage.utils.ElasticClientUtils;
import com.phonepe.columbus.elastic7.storage.ElasticClient;
import com.phonepe.columbus.elastic7.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.commands.search.ScanCommand;
import com.phonepe.columbus.storage.commands.search.ScanCommand.Scanner;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.builder.SearchSourceBuilder;

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
                .toArray(String[]::new);

        final var request = new SearchRequest(indices, buildSearchSource(command, storage))
                .scroll(new Scroll(TimeValue.timeValueSeconds(command.getDurationInSeconds())));

        final var node = Optional.ofNullable(command.getNodeId())
                .map(toolkit.nodeIdResolver()::resolve);
        node.map(ElasticNode::getRouting)
                .filter(CollectionUtils::isNotEmpty)
                .map(routing -> routing.toArray(String[]::new))
                .ifPresent(request::routing);
        node.map(ElasticNode::getPreference).ifPresent(request::preference);

        return request;
    }

    private SearchSourceBuilder buildSearchSource(final ScanCommand<T> command, final E storage) {
        final var toolkit = storage.toolkit();
        final var source = new SearchSourceBuilder()
                .size(command.getBatchSize());

        applyIfNotNull(command.getQuery(), toolkit.queryTransformer()::transform, source::query);

        command.getSorts().stream()
                .map(toolkit.sortTransformer()::transform)
                .forEachOrdered(source::sort);

        applyIfNotNull(command.getProjection(), MiscTransforms::toElastic, source::fetchSource);

        return source;
    }
}
