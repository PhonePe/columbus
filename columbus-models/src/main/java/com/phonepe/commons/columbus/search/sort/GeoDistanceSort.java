package com.phonepe.commons.columbus.search.sort;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.misc.geo.Location;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@JsonClassDescription("Sort that orders documents based on their distance from one or more geographic points")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(GeoDistanceSort.TYPE)
public class GeoDistanceSort extends AbstractSort {

    public static final String TYPE = "GEO_DISTANCE";

    @JsonPropertyDescription("The geo-point field to calculate distance from")
    private String field;

    @JsonPropertyDescription("List of reference geographic points to calculate distance from")
    @Singular(ignoreNullCollections = true)
    private List<@NonNull Location> points;

    @JsonPropertyDescription("How to calculate distance (arc for accurate, plane for faster)")
    private String distanceType;

    @JsonPropertyDescription("Unit of distance (m, km, mi, etc.)")
    private String distanceUnit;

    @JsonPropertyDescription("How to pick the sort value when a document has multiple locations (min, max, avg, median)")
    private String sortMode;

    @JsonPropertyDescription("Whether to ignore documents where the field is missing")
    private Boolean ignoreMissingField;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
