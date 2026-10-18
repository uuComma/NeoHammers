package com.github.uucomma.neohammers.neoforge.plat.registry;

import com.github.uucomma.neohammers.xplat.registry.RegistryHelper;
import com.github.uucomma.neohammers.xplat.registry.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.RegisterEvent;

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
        IEventBus eventBus = ModList.get().getModContainerById(this.getModId()).map(ModContainer::getEventBus).orElseThrow();
        eventBus.addListener(this::registerEvent);
    }

    private void registerEvent(RegisterEvent event) {
        event.register(this.getRegistryKey(), (helper) -> {
            for (RegistrySupplier<T> entry: this.entries) {
                helper.register(entry.getId(), entry.get());
            }
        });
    }
}
