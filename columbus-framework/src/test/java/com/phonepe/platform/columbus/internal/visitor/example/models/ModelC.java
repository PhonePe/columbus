package com.phonepe.platform.columbus.internal.visitor.example.models;

import lombok.NoArgsConstructor;

@NoArgsConstructor(staticName = "of")
public class ModelC implements Model {

    @Override
    public String type() {
        return "C";
    }
}
