package com.github.uucomma.neohammers.fabric.plat.service;

import com.github.uucomma.neohammers.fabric.plat.registry.RegistryHelperImpl;
import com.github.uucomma.neohammers.xplat.registry.RegistryHelper;
import com.github.uucomma.neohammers.xplat.service.RegistryService;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class RegistryServiceImpl implements RegistryService {
    @Override
    public <T> RegistryHelper<T> create(String modId, Registry<T> registry) {
        return new RegistryHelperImpl<>(modId, registry);
    }

    @Override
    public <T> RegistryHelper<T> create(String modId, ResourceKey<? extends Registry<T>> registry) {
        return new RegistryHelperImpl<>(modId, registry);
    }
}
