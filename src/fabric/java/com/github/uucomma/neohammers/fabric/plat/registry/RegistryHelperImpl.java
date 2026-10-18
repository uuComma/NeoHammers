package com.github.uucomma.neohammers.fabric.plat.registry;

import com.github.uucomma.neohammers.xplat.registry.RegistryHelper;
import com.github.uucomma.neohammers.xplat.registry.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class RegistryHelperImpl<T> extends RegistryHelper<T> {
    public RegistryHelperImpl(String modId, Registry<T> registry) {
        super(modId, registry);
    }

    public RegistryHelperImpl(String modId, ResourceKey<? extends Registry<T>> registryKey) {
        super(modId, registryKey);
    }

    @Override
    public void register() {
        this.entries.trimToSize();
        for (RegistrySupplier<T> entry: this.entries) {
            Registry.register(this.getRegistry(), entry.getId(), entry.get());
        }
    }
}
