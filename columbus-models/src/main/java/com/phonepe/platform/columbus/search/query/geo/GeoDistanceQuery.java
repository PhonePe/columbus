package com.phonepe.platform.columbus.search.query.geo;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.search.misc.geo.Location;
import com.phonepe.platform.columbus.search.query.FieldQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(GeoDistanceQuery.TYPE)
public class GeoDistanceQuery extends FieldQuery {

    public static final String TYPE = "GEO_DISTANCE";

    private Location center;

    private String distance;
    private String distanceType;

    private Boolean ignoreMissingField;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
