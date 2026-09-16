package com.github.uucomma.neohammers.common.enchantment;

import com.github.uucomma.neohammers.NeoHammers;
import com.github.uucomma.neohammers.common.util.ModTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;

public final class ModEnchantments {
    public static final ResourceKey<Enchantment> HAMMERING = enchantment("hammering");

    private static ResourceKey<Enchantment> enchantment(String id) {
        return ResourceKey.create(Registries.ENCHANTMENT, NeoHammers.resource(id));
    }

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        var hammersHolderSet = context.lookup(Registries.ITEM).getOrThrow(ModTags.ItemTags.HAMMERS);
        context.register(HAMMERING, Enchantment.enchantment(
                        Enchantment.definition(
                                hammersHolderSet, // Supported Items
                                hammersHolderSet, // Primary Items
                                4, // Weight
                                6, // Max Level
                                Enchantment.dynamicCost(1, 10), // Min Cost
                                Enchantment.dynamicCost(51, 10), // Max Cost
                                2, // Anvil Cost
                                EquipmentSlotGroup.MAINHAND // Equipment Slots
                        )
                ).withEffect(ModEnchantmentEffectComponents.EXTENDED_AREA_MINE.get()).build(HAMMERING.identifier()));
    }
}
