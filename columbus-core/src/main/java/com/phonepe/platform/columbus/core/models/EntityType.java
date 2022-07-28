package com.phonepe.platform.columbus.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EntityType {
    @JsonProperty("category")
    CATEGORY {
        @Override
        public <T> T accept(EntityTypeVisitor<T> entityTypeVisitor) {
            return entityTypeVisitor.visitCategory();
        }
    },

    @JsonProperty("unit")
    UNIT {
        @Override
        public <T> T accept(EntityTypeVisitor<T> entityTypeVisitor) {
            return entityTypeVisitor.visitUnit();
        }
    };

    public abstract <T> T accept(EntityType.EntityTypeVisitor<T> entityTypeVisitor);

    public interface EntityTypeVisitor<T> {
        T visitCategory();

        T visitUnit();
    }
}
