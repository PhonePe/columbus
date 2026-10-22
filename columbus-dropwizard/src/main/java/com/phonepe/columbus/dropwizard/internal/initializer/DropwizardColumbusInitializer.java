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
package com.phonepe.columbus.dropwizard.internal.initializer;

import com.phonepe.columbus.common.initializer.Initializer;
import com.phonepe.columbus.common.initializer.LateInit;
import com.phonepe.columbus.dropwizard.initializer.DropwizardInitializer;
import com.phonepe.columbus.dropwizard.storage.DropwizardStorages;
import com.phonepe.columbus.internal.core.initializer.DefaultColumbusInitializer;
import com.phonepe.columbus.storage.factory.StorageClientFactory;
import com.phonepe.columbus.storage.settings.StorageSettings;
import io.dropwizard.setup.Environment;

import java.util.Objects;

/**
 * An object that configures Columbus initializer to utilize Dropwizard's features.
 */
public class DropwizardColumbusInitializer extends DefaultColumbusInitializer implements DropwizardInitializer {

    private final LateInit<Environment> environment = new LateInit<>("environment");

    @Override
    public void use(final Environment environment) {
        this.environment.set(environment);
    }

    public Initializer<Environment> getEnvironment() {
        return environment;
    }

    @Override
    protected boolean canInitializeStorageClientFactory() {
        return super.canInitializeStorageClientFactory() && getEnvironment().isPresent();
    }

    @Override
    protected StorageClientFactory initializeStorageClientFactory() {
        return DropwizardStorages.storageClientFactory(
                Objects.requireNonNullElseGet(getSettings().get().getStorageSettings(), StorageSettings::defaults),
                getToolkit().get(),
                getEnvironment().get());
    }
}
