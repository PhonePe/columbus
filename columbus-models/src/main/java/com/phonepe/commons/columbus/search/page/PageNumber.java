package com.phonepe.commons.columbus.search.page;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(PageNumber.TYPE)
public class PageNumber extends BasePageBounds {

    public static final String TYPE = "NUMBER";

    private Integer number;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
