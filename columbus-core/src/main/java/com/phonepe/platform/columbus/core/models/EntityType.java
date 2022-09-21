package com.phonepe.platform.columbus.core.models;


public enum EntityType {
    CATEGORY {
        @Override
        public <T> T accept(EntityTypeVisitor<T> entityTypeVisitor) {
            return entityTypeVisitor.visitCategory();
        }
    },

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
