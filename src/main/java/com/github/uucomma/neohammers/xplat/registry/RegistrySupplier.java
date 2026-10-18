package com.github.uucomma.neohammers.xplat.registry;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

public interface RegistrySupplier<T> extends Supplier<T> {
    Holder<T> getHolder();
    Identifier getId();
    ResourceKey<T> getKey();
}
