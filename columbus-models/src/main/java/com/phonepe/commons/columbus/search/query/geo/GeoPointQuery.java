package com.phonepe.commons.columbus.search.query.geo;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.misc.geo.Location;
import com.phonepe.commons.columbus.search.query.FieldQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(GeoPointQuery.TYPE)
public class GeoPointQuery extends FieldQuery {

    public static final String TYPE = "GEO_POINT";

    private Location point;

    private Boolean ignoreMissingField;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
