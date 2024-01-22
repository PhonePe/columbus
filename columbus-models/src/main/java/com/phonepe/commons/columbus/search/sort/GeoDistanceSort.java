package com.phonepe.commons.columbus.search.sort;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.misc.geo.Location;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(GeoDistanceSort.TYPE)
public class GeoDistanceSort extends AbstractSort {

    public static final String TYPE = "GEO_DISTANCE";

    private String field;

    @Singular(ignoreNullCollections = true)
    private List<@NonNull Location> points;

    private String distanceType;
    private String distanceUnit;

    private String sortMode;

    private Boolean ignoreMissingField;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
