package com.phonepe.platform.columbus.internal.visitor;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

abstract class Model {

    abstract String type();

    @Data
    @Builder
    static class TypeA extends Model {

        private final Integer value;

        @Override
        String type() {
            return "A";
        }
    }

    @Data
    @Builder
    static class TypeB extends Model {

        private final String value;

        @Override
        String type() {
            return "B";
        }
    }

    @NoArgsConstructor(staticName = "of")
    static class TypeC extends Model {

        @Override
        String type() {
            return "C";
        }
    }
}
