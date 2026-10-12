package com.phonepe.columbus.internal.visitor.example.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModelA implements Model {

    private final Integer value;

    @Override
    public String type() {
        return "A";
    }
}
