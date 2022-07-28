package com.phonepe.platform.columbus.core.search.resolver.impl;

import com.phonepe.platform.columbus.core.search.resolver.FilterResolver;
import com.phonepe.platform.columbus.core.search.resolver.models.ResolvedFilter;
import com.phonepe.platform.columbus.core.search.value.ValueGetter;
import com.phonepe.platform.columbus.models.core.search.filter.Filter;
import com.phonepe.platform.columbus.models.core.search.filter.general.EqualsFilter;
import com.phonepe.platform.columbus.models.core.search.filter.general.InFilter;
import com.phonepe.platform.columbus.models.core.search.filter.general.NotEqualsFilter;
import com.phonepe.platform.columbus.models.core.search.filter.range.BetweenFilter;
import com.phonepe.platform.columbus.models.core.search.filter.range.GreaterEqualsFilter;
import com.phonepe.platform.columbus.models.core.search.filter.range.GreaterThanFilter;
import com.phonepe.platform.columbus.models.core.search.filter.range.LessEqualsFilter;
import com.phonepe.platform.columbus.models.core.search.filter.range.LessThanFilter;
import com.phonepe.platform.columbus.models.core.search.query.FilterSelection;
import com.phonepe.platform.columbus.models.core.search.query.value.BetweenRange;
import com.phonepe.platform.columbus.models.core.search.value.ReferencedValue;
import com.phonepe.platform.columbus.models.core.search.value.Value;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class DefaultFilterResolver implements FilterResolver {
    private static final String PREFIX = "#";
    private static final String SEPARATOR = ":";
    private static final Pattern BETWEEN_INCLUSION = Pattern.compile("^<=?$");

    @Override
    public Optional<ResolvedFilter> resolve(FilterSelection selection) {
        String name = selection.getFilterName();
        // Resolves the filters supported by Columbus: the names start with #
        if (name.startsWith(PREFIX)) {
            return Optional.ofNullable(parseFieldFilter(selection))
                .map(filter -> ResolvedFilter.builder().filter(filter).build());
        }
        return Optional.empty();
    }

    private Filter parseFieldFilter(FilterSelection selection) {
        String name = selection.getFilterName();
        // Field filters are of the form <op>:<field>[:extras]
        String[] splits = name.substring(1).split(SEPARATOR);
        if (splits.length == 2) {
            String operator = splits[0];
            String field = splits[1];
            return switch (operator.toLowerCase()) {
                case "=" -> createEqualsFilter(selection.getValue(), field);
                case "!=" -> createNotEqualsFilter(selection.getValue(), field);
                case "in" -> createInFilter(selection.getValue(), field);
                case "<" -> createLessThanFilter(selection.getValue(), field);
                case "<=" -> createLessEqualsFilter(selection.getValue(), field);
                case ">" -> createGreaterThanFilter(selection.getValue(), field);
                case ">=" -> createGreaterEqualsFilter(selection.getValue(), field);
                default -> null;
            };
        } else if (splits.length == 3) {
            return parseBetweenFilter(selection.getValue(), splits);
        }
        return null;
    }

    private Filter createEqualsFilter(Value<Object> value, String field) {
        return EqualsFilter.builder()
            .field(field)
            .value(value)
            .build();
    }

    private Filter createNotEqualsFilter(Value<Object> value, String field) {
        return NotEqualsFilter.builder()
            .field(field)
            .value(value)
            .build();
    }

    private Filter createInFilter(Value<Object> value, String field) {
        Value<List<Object>> values = null;
        if (value instanceof ReferencedValue) {
            values = Value.referenced(((ReferencedValue<Object>) value).getName());
        } else {
            Object valueList = value.accept(new ValueGetter<>());
            if (valueList instanceof List) {
                //noinspection unchecked
                values = Value.exact((List<Object>) valueList);
            }
        }
        if (values == null) {
            return null;
        }
        return InFilter.builder()
            .field(field)
            .values(values)
            .build();
    }

    private Filter createLessThanFilter(Value<Object> value, String field) {
        return LessThanFilter.builder()
            .field(field)
            .value(value)
            .build();
    }

    private Filter createLessEqualsFilter(Value<Object> value, String field) {
        return LessEqualsFilter.builder()
            .field(field)
            .value(value)
            .build();
    }

    private Filter createGreaterThanFilter(Value<Object> value, String field) {
        return GreaterThanFilter.builder()
            .field(field)
            .value(value)
            .build();
    }

    private Filter createGreaterEqualsFilter(Value<Object> value, String field) {
        return GreaterEqualsFilter.builder()
            .field(field)
            .value(value)
            .build();
    }

    private Filter parseBetweenFilter(Value<Object> value, String[] splits) {
        // Between filter is of the form lower:field:upper, where lower and upper are < or <=
        if (BETWEEN_INCLUSION.matcher(splits[0]).matches()
            && BETWEEN_INCLUSION.matcher(splits[2]).matches()) {
            Object rangeValue = value.accept(new ValueGetter<>());
            if (rangeValue instanceof BetweenRange range) {
                return BetweenFilter.builder()
                    .field(splits[1])
                    .from(range.getFrom())
                    .to(range.getTo())
                    .includeLower("<=".equals(splits[0]))
                    .includeUpper("<=".equals(splits[2]))
                    .build();
            }
        }
        return null;
    }
}
