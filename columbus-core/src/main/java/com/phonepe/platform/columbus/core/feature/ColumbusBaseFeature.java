package com.phonepe.platform.columbus.core.feature;

import io.dropwizard.Configuration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ColumbusBaseFeature<T extends Configuration, O extends ColumbusBaseFeature.Options<T>> implements ColumbusFeature<T> {
    @Getter(AccessLevel.PROTECTED)
    private final O options;

    public interface Options<T extends Configuration> {
        ColumbusFeature<T> toFeature();
    }
}
