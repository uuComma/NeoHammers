package com.github.uucomma.neohammers.common.enchantment;

import com.github.uucomma.neohammers.NeoHammers;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Unit;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModEnchantmentEffectComponents {
    private static final DeferredRegister<DataComponentType<?>> REGISTER = DeferredRegister.create(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, NeoHammers.MOD_ID);

    public static final Supplier<DataComponentType<Unit>> EXTENDED_AREA_MINE = register("extended_area_mine",
            () -> DataComponentType.<Unit>builder().persistent(Unit.CODEC).build());

    private static<T> Supplier<DataComponentType<T>> register(String id, Supplier<DataComponentType<T>> sup) {
        return REGISTER.register(id, sup);
    }

    public static void register(IEventBus eventBus) {
        REGISTER.register(eventBus);
        NeoHammers.LOGGER.info("Registered Enchantment Effect Components.");
    }
}
