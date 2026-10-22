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
package com.phonepe.columbus.internal.reflect;

import com.phonepe.columbus.common.Constants;
import com.phonepe.columbus.reflect.ReflectSettings;
import com.phonepe.columbus.reflect.ReflectionInitializer;
import org.reflections.Reflections;

import java.util.List;

/**
 * Creates {@link org.reflections.Reflections Reflections} with the packages and classes provided by
 * {@link ReflectSettings}, along with the base package(s) of Columbus.
 */
public class SimpleReflectionInitializer implements ReflectionInitializer {

    @Override
    public Reflections initialize(final ReflectSettings settings) {
        return new Reflections(getDefaultPackages(), settings.getPackageNames(), settings.getClasses());
    }

    protected List<String> getDefaultPackages() {
        return List.of(Constants.BASE_PACKAGE);
    }
}
