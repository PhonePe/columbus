package com.phonepe.columbus.search.misc.geo;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Location {

    private Double lat;

    private Double lon;
}
