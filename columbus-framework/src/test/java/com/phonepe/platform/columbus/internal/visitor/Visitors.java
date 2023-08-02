package com.phonepe.platform.columbus.internal.visitor;

import com.phonepe.platform.columbus.internal.visitor.Model.TypeA;
import com.phonepe.platform.columbus.internal.visitor.Model.TypeB;
import com.phonepe.platform.columbus.visitor.impl.EntityVisitor;
import com.phonepe.platform.columbus.visitor.impl.VisitorContext;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

interface Visitors {

    interface NameVisitor<T extends Model> extends EntityVisitor<String, T, Model, NameVisitorContext> {

        @Override
        String visit(T model, NameVisitorContext context);
    }

    @SuperBuilder(toBuilder = true)
    class NameVisitorContext extends VisitorContext<String, Model, NameVisitorContext> {

        @Override
        protected @NonNull NameVisitorContext self() {
            return this;
        }
    }

    class TypeANameVisitor implements NameVisitor<TypeA> {

        @Override
        public String visit(final TypeA model, final NameVisitorContext context) {
            return "%s(value=%d)".formatted(model.type(), model.getValue());
        }
    }

    class TypeBNameVisitor implements NameVisitor<TypeB> {

        @Override
        public String visit(final TypeB model, final NameVisitorContext context) {
            return "%s(value='%s')".formatted(model.type(), model.getValue());
        }
    }
}
