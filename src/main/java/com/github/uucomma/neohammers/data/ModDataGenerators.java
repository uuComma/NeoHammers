package com.github.uucomma.neohammers.data;

import com.github.uucomma.neohammers.NeoHammers;
import com.github.uucomma.neohammers.common.ModVillagerTrades;
import com.github.uucomma.neohammers.common.enchantment.ModEnchantmentEffectComponents;
import com.github.uucomma.neohammers.common.enchantment.ModEnchantments;
import com.github.uucomma.neohammers.common.util.ModTags;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.trading.TradeCost;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Optional;

@EventBusSubscriber(modid = NeoHammers.MOD_ID)
public final class ModDataGenerators {
    private static final RegistrySetBuilder DATAPACK_REGISTRY_PROVIDERS = new RegistrySetBuilder()
            .add(Registries.ENCHANTMENT, (context) -> {
                var hammersHolderSet = context.lookup(Registries.ITEM).get(ModTags.ItemTags.HAMMERS).orElseThrow();
                context.register(ModEnchantments.HAMMERING, Enchantment.enchantment(
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
                ).withEffect(ModEnchantmentEffectComponents.EXTENDED_AREA_MINE.get())
                        .build(ModEnchantments.HAMMERING.identifier()));
            }).add(Registries.VILLAGER_TRADE, (context) -> {
                var enchantment = context.lookup(Registries.ENCHANTMENT).get(ModEnchantments.HAMMERING).orElseThrow();
                context.register(ModVillagerTrades.LIBRARIAN_HAMMERING, new VillagerTrade(
                        new TradeCost(Items.EMERALD, UniformGenerator.between(15, 55)),
                        new ItemStackTemplate(Items.BOOK),
                        6,
                        3,
                        0.05f,
                        Optional.empty(),
                        List.of(new SetEnchantmentsFunction.Builder().withEnchantment(enchantment, UniformGenerator.between(1, 3)).build())
                ));
            });

    @SubscribeEvent
    public static void onGatherClientData(GatherDataEvent.Client event) {
        // Assets
        event.createProvider(ModModelProvider::new);

        // Data
        event.createDatapackRegistryObjects(DATAPACK_REGISTRY_PROVIDERS);
        event.createProvider(ModEnchantmentTagsProvider::new);
        event.createProvider(ModVillagerTradeTagsProvider::new);
        event.createProvider(ModBlockTagsProvider::new);
        event.createProvider(ModItemTagsProvider::new);
        event.createProvider(ModGlobalLootModifierProvider::new);
        event.createProvider(ModRecipeProvider.Runner::new);
        event.createProvider(ModAdvancementProvider::new);
        event.createProvider(ModDataMapProvider::new);
    }
}
