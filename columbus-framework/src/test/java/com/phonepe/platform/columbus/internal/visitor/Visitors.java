package com.phonepe.platform.columbus.internal.visitor;

import com.phonepe.platform.columbus.internal.visitor.Model.TypeA;
import com.phonepe.platform.columbus.internal.visitor.Model.TypeB;
import com.phonepe.platform.columbus.internal.visitor.Model.TypeC;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.visitor.impl.EntityVisitor;
import com.phonepe.platform.columbus.visitor.impl.VisitorContext;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuppressWarnings("ClassEscapesDefinedScope")
interface Visitors {

    interface BaseNameVisitor<T extends Model, C extends BaseNameVisitorContext<C>>
            extends EntityVisitor<String, T, Model, C> {
    }

    interface NameVisitor<T extends Model> extends BaseNameVisitor<T, NameVisitorContext> {

        @Override
        String visit(T model, NameVisitorContext context);
    }

    @SuperBuilder(toBuilder = true)
    abstract class BaseNameVisitorContext<C extends BaseNameVisitorContext<C>>
            extends VisitorContext<String, Model, C> {
    }

    @SuperBuilder(toBuilder = true)
    class NameVisitorContext extends BaseNameVisitorContext<NameVisitorContext> {

        @Override
        protected @NonNull NameVisitorContext self() {
            return this;
        }
    }

    @SuperBuilder(toBuilder = true)
    class DummyNameVisitorContext extends BaseNameVisitorContext<DummyNameVisitorContext> {

        @Override
        protected @NonNull DummyNameVisitorContext self() {
            return this;
        }
    }

    @RegistryKey(name = "A", type = TypeA.class)
    class TypeANameVisitor implements NameVisitor<TypeA> {

        @Override
        public String visit(final TypeA model, final NameVisitorContext context) {
            return "%s(value=%d)".formatted(model.type(), model.getValue());
        }
    }

    @RegistryKey(name = "B", type = TypeB.class)
    class TypeBNameVisitor implements NameVisitor<TypeB> {

        @Override
        public String visit(final TypeB model, final NameVisitorContext context) {
            return "%s(value='%s')".formatted(model.type(), model.getValue());
        }
    }

    @RegistryKey(name = "C", type = TypeC.class)
    class TypeCNameVisitor<C extends BaseNameVisitorContext<C>> implements BaseNameVisitor<TypeC, C> {

        @Override
        public String visit(final TypeC value, final C context) {
            return "%s(%s)".formatted(value.type(), context.getClass().getSimpleName());
        }
    }
}
