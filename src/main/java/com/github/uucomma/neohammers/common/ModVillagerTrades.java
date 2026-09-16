package com.github.uucomma.neohammers.common;

import com.github.uucomma.neohammers.NeoHammers;
import com.github.uucomma.neohammers.common.enchantment.ModEnchantments;
import net.minecraft.advancements.predicates.DataComponentMatchers;
import net.minecraft.advancements.predicates.EnchantmentPredicate;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.predicates.DataComponentPredicates;
import net.minecraft.core.component.predicates.EnchantmentsPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.trading.TradeCost;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.item.trading.VillagerTrades;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

import java.util.List;
import java.util.Optional;

public final class ModVillagerTrades {
    public static final ResourceKey<VillagerTrade> LIBRARIAN_HAMMERING = trade("librarian/hammering");

    private static ResourceKey<VillagerTrade> trade(String id) {
        return ResourceKey.create(Registries.VILLAGER_TRADE, NeoHammers.resource(id));
    }

    public static void bootstrap(BootstrapContext<VillagerTrade> context) {
        var itemLookup = context.lookup(Registries.ITEM);
        var enchantmentLookup = context.lookup(Registries.ENCHANTMENT);
        var enchantment = enchantmentLookup.getOrThrow(ModEnchantments.HAMMERING);
        context.register(ModVillagerTrades.LIBRARIAN_HAMMERING, VillagerTrade.builder(
                new TradeCost(Items.EMERALD, ContextIntProviders.between(15, 55)),
                new ItemStackTemplate(Items.BOOK),
                3,
                15,
                0
        ).addModifiers(enchantedBook(itemLookup, enchantment, 1, 3)).build());
    }

    private static List<Holder<LootItemFunction>> enchantedBook(HolderGetter<Item> items, Holder<Enchantment> enchantment, int minLevel, int maxLevel) {
        ItemPredicate.Builder bookWithExactLevelEnchants = new ItemPredicate.Builder()
                .of(items, Items.ENCHANTED_BOOK)
                .withComponents(
                        DataComponentMatchers.Builder.components()
                                .partial(
                                        DataComponentPredicates.STORED_ENCHANTMENTS,
                                        EnchantmentsPredicate.storedEnchantments(List.of(new EnchantmentPredicate(Optional.empty(), MinMaxBounds.Ints.between(minLevel, maxLevel))))
                                )
                                .build()
                );

        return VillagerTrades.discardItemIfItsNot(new SetEnchantmentsFunction.Builder().withEnchantment(enchantment, ContextIntProviders.between(minLevel, maxLevel)), bookWithExactLevelEnchants);
    }
}
