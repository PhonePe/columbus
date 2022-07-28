package com.phonepe.platform.columbus.core.search.value;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReferenceMapEntry {
    @NotBlank
    private String name;

    private Object value;
}
