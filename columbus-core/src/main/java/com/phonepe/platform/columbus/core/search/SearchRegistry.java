package com.phonepe.platform.columbus.core.search;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.platform.columbus.core.exception.ColumbusException;
import com.phonepe.platform.columbus.core.search.preprocessor.Preprocessor;
import com.phonepe.platform.columbus.models.core.exceptions.ColumbusCoreErrorCode;
import com.phonepe.platform.columbus.models.core.search.filter.Filter;
import com.phonepe.platform.columbus.models.core.search.sorter.Sorter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class SearchRegistry {
    private final ObjectMapper mapper;
    private final Map<String, Class<? extends Filter>> filters = new HashMap<>();
    private final Map<String, Class<? extends Sorter>> sorters = new HashMap<>();
    private final Map<String, Class<? extends Preprocessor>> preprocessors = new HashMap<>();

    public void registerFilters(Collection<Class<? extends Filter>> filters) {
        registerClasses(this.filters, filters, "filter");
    }

    public void registerSorters(Collection<Class<? extends Sorter>> sorters) {
        registerClasses(this.sorters, sorters, "sorter");
    }

    public void registerPreprocessors(Collection<Class<? extends Preprocessor>> preprocessors) {
        registerClasses(this.preprocessors, preprocessors, "preprocessor");
    }

    private <T> void registerClasses(
        Map<String, Class<? extends T>> map,
        Collection<Class<? extends T>> classes,
        String entityName
    ) {
        List<Class<? extends T>> classesToRegister = new ArrayList<>();
        for (Class<? extends T> cls : classes) {
            String name = cls.getAnnotation(JsonTypeName.class).value();
            if (map.containsKey(name)) {
                Class<? extends T> current = map.get(name);
                if (!current.equals(cls)) {
                    log.error("Duplicate {} found for name '{}', {}: {} was already registered",
                        entityName, name, cls, current
                    );
                    throw ColumbusException.error(ColumbusCoreErrorCode.DUPLICATE_ENTITY,
                        "Duplicate %s found for name '%s', %s: %s was already registered".formatted(entityName, name, cls, current));
                }
                log.warn("{} already registered for name '{}': {}", entityName, name, cls);
            } else {
                map.put(name, cls);
                classesToRegister.add(cls);
            }
        }
        register(new ArrayList<>(classesToRegister));
    }

    public void register(Collection<Class<?>> classes) {
        mapper.registerSubtypes(classes);
    }
}
