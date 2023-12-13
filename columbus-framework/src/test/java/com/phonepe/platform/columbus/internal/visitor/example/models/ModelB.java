package com.phonepe.platform.columbus.internal.visitor.example.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModelB implements Model {

    private final String value;

    @Override
    public String type() {
        return "B";
    }
}
