package com.github.uucomma.neohammers.common.enchantment;

import com.github.uucomma.neohammers.NeoHammers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

public final class ModEnchantments {
    public static final ResourceKey<Enchantment> HAMMERING = enchantment("hammering");

    private static ResourceKey<Enchantment> enchantment(String id) {
        return ResourceKey.create(Registries.ENCHANTMENT, NeoHammers.resource(id));
    }
}
