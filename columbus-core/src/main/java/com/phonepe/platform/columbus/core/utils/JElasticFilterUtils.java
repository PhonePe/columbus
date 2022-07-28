package com.phonepe.platform.columbus.core.utils;

import com.grookage.jelastic.core.models.query.filter.Filter;
import com.grookage.jelastic.core.models.query.filter.general.EqualsFilter;
import com.grookage.jelastic.core.models.query.filter.general.InFilter;
import com.grookage.jelastic.core.models.query.filter.general.NotEqualsFilter;
import com.grookage.jelastic.core.models.query.filter.number.BetweenFilter;
import com.grookage.jelastic.core.models.query.filter.number.GreaterEqualsFilter;
import com.grookage.jelastic.core.models.query.filter.number.GreaterThanFilter;
import com.grookage.jelastic.core.models.query.filter.number.LesserEqualsFilter;
import com.grookage.jelastic.core.models.query.filter.number.LesserThanFilter;
import com.grookage.jelastic.core.models.query.filter.predicate.AndFilter;
import com.grookage.jelastic.core.models.query.filter.predicate.ORFilter;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class JElasticFilterUtils {
    public EqualsFilter equalsFilter(String field, Object value) {
        return new EqualsFilter(field, value);
    }

    public NotEqualsFilter notEqualsFilter(String field, Object value) {
        // Constructor only accepts string
        NotEqualsFilter filter = new NotEqualsFilter();
        filter.setFieldName(field);
        filter.setValue(value);
        return filter;
    }

    public <T> InFilter<T> inFilter(String field, List<T> values) {
        return new InFilter<>(field, values);
    }

    public AndFilter andFilter(List<Filter> filters) {
        return new AndFilter(filters);
    }

    public ORFilter orFilter(List<Filter> filters) {
        return new ORFilter(filters);
    }

    public GreaterThanFilter greaterThan(String field, Object value) {
        return new GreaterThanFilter(field, parseNumber(value), false, true);
    }

    public LesserThanFilter lessThan(String field, Object value) {
        return new LesserThanFilter(field, parseNumber(value), true, false);
    }

    public GreaterEqualsFilter greaterEquals(String field, Object value) {
        return new GreaterEqualsFilter(field, parseNumber(value), true, true);
    }

    public LesserEqualsFilter lesserEquals(String field, Object value) {
        return new LesserEqualsFilter(field, parseNumber(value), true, true);
    }

    public BetweenFilter between(String field, Object from, Object to) {
        return new BetweenFilter(field, parseNumber(from), parseNumber(to));
    }

    private Number parseNumber(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number number) {
            return number;
        }
        return Double.parseDouble(String.valueOf(value));
    }
}
