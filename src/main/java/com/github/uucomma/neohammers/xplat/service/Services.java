package com.github.uucomma.neohammers.xplat.service;

import com.github.uucomma.neohammers.NeoHammers;

import java.util.ServiceLoader;

public final class Services {
    public static final PlatformService PLATFORM = load(PlatformService.class);
    public static final RegistryService REGISTRY = load(RegistryService.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz, Services.class.getClassLoader())
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        NeoHammers.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
