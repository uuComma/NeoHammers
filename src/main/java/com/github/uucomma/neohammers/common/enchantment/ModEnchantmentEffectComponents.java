package com.github.uucomma.neohammers.common.enchantment;

import com.github.uucomma.neohammers.NeoHammers;
import com.github.uucomma.neohammers.xplat.registry.RegistryHelper;
import com.github.uucomma.neohammers.xplat.registry.RegistrySupplier;
import com.github.uucomma.neohammers.xplat.service.Services;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Unit;

public final class ModEnchantmentEffectComponents {
    private static final RegistryHelper<DataComponentType<?>> HELPER = Services.REGISTRY.create(NeoHammers.MOD_ID, Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE);

    public static final RegistrySupplier<DataComponentType<Unit>> EXTENDED_AREA_MINE = HELPER.register("extended_area_mine",
            () -> DataComponentType.<Unit>builder().persistent(Unit.CODEC).build());

    public static void register() {
        HELPER.register();
        NeoHammers.LOGGER.info("Registered Enchantment Effect Components.");
    }
}
