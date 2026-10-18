package com.github.uucomma.neohammers.xplat.service;

import com.github.uucomma.neohammers.xplat.registry.RegistryHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface RegistryService {
    <T> RegistryHelper<T> create(String modId, Registry<T> registry);
    <T> RegistryHelper<T> create(String modId, ResourceKey<? extends Registry<T>> registry);
}
