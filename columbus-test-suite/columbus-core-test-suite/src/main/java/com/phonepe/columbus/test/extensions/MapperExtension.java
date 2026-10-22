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
package com.phonepe.columbus.test.extensions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.columbus.test.annotations.WithMapper;
import com.phonepe.columbus.test.value.AnnotatedValueExtension;
import com.phonepe.columbus.test.value.ValueProps;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Optional;

public class MapperExtension extends AnnotatedValueExtension<ObjectMapper> {

    @Override
    protected ObjectMapper initialize(final ExtensionContext context) {
        return new ObjectMapper();
    }

    @Override
    protected Class<ObjectMapper> getType() {
        return ObjectMapper.class;
    }

    @Override
    protected Optional<ValueProps> getProps(final ExtensionContext context) {
        return AnnotationSupport.findAnnotation(context.getTestClass(), WithMapper.class)
                .map(WithMapper::value);
    }
}
