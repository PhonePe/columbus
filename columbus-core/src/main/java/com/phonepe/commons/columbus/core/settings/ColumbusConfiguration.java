package com.phonepe.commons.columbus.core.settings;

import com.phonepe.commons.columbus.storage.StorageConfiguration;

import java.util.List;

/**
 * A collection of parameters to configure {@link com.phonepe.commons.columbus.Columbus Columbus}.
 *
 * @apiNote This class is expected to have those parameters which can affect the behaviour of the module at runtime,
 * without requiring any code changes. Parameters that do not meet these criteria can be added to
 * {@link ColumbusSettings}.
 * @implNote The implementers are expected to handle the creation of the objects, and / or serialization and
 * deserialization of the attributes depending on the framework being integrated.
 * @see ColumbusSettings
 */
public interface ColumbusConfiguration {

    boolean isEnabled();

    List<StorageConfiguration> getStorages();
}
