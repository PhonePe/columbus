/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phonepe.columbus.reflect;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.reflect.TypeUtils;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;

/**
 * A set of utility functions operating on {@link Type} and other related classes.
 */
@UtilityClass
public class Generics {

    /**
     * Add exact type to a class instance, for static type analysis. This helps get rid of unchecked casts due to
     * type erasure.
     * <p>
     * For example, {@code List.class} would be of type {@code Class<List<?>>}, which does not match the expected
     * {@code Class<List<T>>} for some specific type {@code T}. Wrapping it with this method would ensure type
     * inference would work as expected.
     * <p>
     * Note that this does not add any type information to the input parameter.
     *
     * @param type the raw class instance.
     * @param <T>  the type represented by the input class.
     * @return the input with a defined type parameter.
     */
    @SuppressWarnings("unchecked")
    public <T> Class<T> typed(final Class<?> type) {
        return (Class<T>) type;
    }

    /**
     * Check if a type is a super type of another.
     *
     * @param superType the parent / ancestor type.
     * @param subType   the child / inherited type.
     * @return true if the subType is a child of superType, false otherwise.
     */
    public boolean isSuperType(final Type superType, final Type subType) {
        return TypeUtils.isAssignable(subType, superType);
    }

    /**
     * Get the type representing the type parameter of a generic class. Useful to identify the actual type or bounds
     * of a type parameter. If the input {@code type} is generic and does not specify a concrete type for the type
     * parameter, the method returns the tightest bound provided.
     * <p>
     * For example, consider a generic interface:<br/>
     * <code>
     * interface GenericCollection&lt;V, T extends Collection&lt;V&gt;&gt; {
     * }
     * </code>
     * <p>
     * and a class that implements it for integer collections:
     * <br/>
     * <code>
     * interface IntegerCollection&lt;T extends Collection&lt;Integer&gt;&gt; extends GenericCollection&lt;Integer,
     * T&gt; {
     * }
     * </code>
     * <p>
     * and an actual implementation that uses lists:
     * <br/>
     * <code>
     * class IntegerList implements IntegerCollection&lt;List&lt;Integer&gt;&gt; {
     * }
     * </code>
     * <p>
     * This method helps to answer questions like:
     * <ul>
     *     <li>
     *         which type of collections does {@code IntegerList} use?<br/>
     *         <code>getTypeParameter(IntegerList.class, GenericCollection.class.getTypeParameters()[1])</code>
     *     </li>
     *     <li>
     *         which type of values does {@code IntegerList} store?<br/>
     *         <code>getTypeParameter(IntegerList.class, GenericCollection.class.getTypeParameters()[0])</code>
     *     </li>
     * </ul>
     *
     * @param type     the type whose parameter needs to be determined.
     * @param variable the parameter whose value needs to be determined.
     * @param <T>      the type represented by the type parameter.
     * @return the actual type or the bounds of {@code variable}, as defined by {@code type}.
     */
    public <T> Type getTypeParameter(final Type type, final TypeVariable<Class<T>> variable) {
        final var arguments = TypeUtils.getTypeArguments(type, variable.getGenericDeclaration());
        final var parameter = arguments.get(variable);
        if (parameter instanceof TypeVariable<?> typeVariable) {
            return TypeUtils.getImplicitBounds(typeVariable)[0];
        }
        return parameter;
    }

    /**
     * Get the super type of the given type that also extends or implements the given ancestor type. Useful to
     * traverse hierarchies bottom up.
     * <p>
     * The highest priority is given to the class that is extended by the given type. Then the list of interfaces are
     * checked in the order provided by the class.
     *
     * @param type     the type whose super type must be found.
     * @param ancestor the bound to be met by the super type.
     * @return the nearest super type of {@code type} which is a subtype of {@code ancestor}, or null if no such type
     * is found.
     * @throws NullPointerException if {@code type} is {@code null}.
     * @implNote If the ancestor is null, the behaviour is as if the ancestor is {@link Object}.
     */
    public Class<?> getSuperTypeTowards(final Class<?> type, final Class<?> ancestor) {
        final var superType = type.getSuperclass();
        if (ancestor == null) {
            return superType;
        }
        if (superType != null && ancestor.isAssignableFrom(superType)) {
            return superType;
        }
        return Arrays.stream(type.getInterfaces())
                .filter(ancestor::isAssignableFrom)
                .findFirst()
                .orElse(null);
    }
}
