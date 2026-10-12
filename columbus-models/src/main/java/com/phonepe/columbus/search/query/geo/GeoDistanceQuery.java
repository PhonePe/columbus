package com.phonepe.columbus.search.query.geo;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.search.misc.geo.Location;
import com.phonepe.columbus.search.query.FieldQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Query that matches documents where a geo-point field is within a specified distance from a central location")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(GeoDistanceQuery.TYPE)
public class GeoDistanceQuery extends FieldQuery {

    public static final String TYPE = "GEO_DISTANCE";

    @JsonPropertyDescription("The central geographic location (latitude/longitude) from which to measure distance")
    private Location center;

    @JsonPropertyDescription("Maximum distance from center (e.g., '10km', '5mi', '100m')")
    private String distance;

    @JsonPropertyDescription("How to calculate distance (arc for accurate, plane for faster but less accurate)")
    private String distanceType;

    @JsonPropertyDescription("Whether to ignore documents where the field is missing")
    private Boolean ignoreMissingField;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
