package com.github.uucomma.neohammers.neoforge.loot;

import com.github.uucomma.neohammers.NeoHammers;
import com.github.uucomma.neohammers.xplat.registry.RegistryHelper;
import com.github.uucomma.neohammers.xplat.registry.RegistrySupplier;
import com.github.uucomma.neohammers.xplat.service.Services;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ModLootModifiers {
    private static final RegistryHelper<MapCodec<? extends IGlobalLootModifier>> HELPER = Services.REGISTRY.create(NeoHammers.MOD_ID, NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS);

    public static final RegistrySupplier<MapCodec<AddEnchantedBookLootModifier>> ADD_ENCHANTED_BOOK = HELPER.register("add_enchanted_book",
            () -> AddEnchantedBookLootModifier.CODEC);

    public static void register() {
        HELPER.register();
        NeoHammers.LOGGER.info("Registered Global Loot Modifiers.");
    }
}
