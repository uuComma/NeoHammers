package com.github.uucomma.neohammers.common.loot;

import com.github.uucomma.neohammers.NeoHammers;
import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class ModLootModifiers {
    private static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> REGISTER = DeferredRegister.create(NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS, NeoHammers.MOD_ID);

    public static final Supplier<MapCodec<AddEnchantedBookLootModifier>> ADD_ENCHANTED_BOOK = register("add_enchanted_book",
            () -> AddEnchantedBookLootModifier.CODEC);

    private static<T extends IGlobalLootModifier> Supplier<MapCodec<T>> register(String id, Supplier<MapCodec<T>> sup) {
        return REGISTER.register(id, sup);
    }

    public static void register(IEventBus eventBus) {
        REGISTER.register(eventBus);
        NeoHammers.LOGGER.info("Registered Global Loot Modifiers.");
    }
}
