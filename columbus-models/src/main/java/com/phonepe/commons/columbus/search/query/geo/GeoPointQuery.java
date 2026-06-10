package com.phonepe.commons.columbus.search.query.geo;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.misc.geo.Location;
import com.phonepe.commons.columbus.search.query.FieldQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Query that matches documents where a geo-point field contains an exact location")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(GeoPointQuery.TYPE)
public class GeoPointQuery extends FieldQuery {

    public static final String TYPE = "GEO_POINT";

    @JsonPropertyDescription("The exact geographic location (latitude/longitude) to match")
    private Location point;

    @JsonPropertyDescription("Whether to ignore documents where the field is missing")
    private Boolean ignoreMissingField;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
