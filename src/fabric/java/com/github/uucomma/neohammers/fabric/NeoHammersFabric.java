package com.github.uucomma.neohammers.fabric;

import com.github.uucomma.neohammers.NeoHammers;
import com.github.uucomma.neohammers.common.enchantment.ModEnchantments;
import com.github.uucomma.neohammers.common.event.ModEvents;
import com.github.uucomma.neohammers.common.item.ModItems;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import net.neoforged.fml.config.ModConfig;
import org.jspecify.annotations.Nullable;

public final class NeoHammersFabric implements ModInitializer  {
    @Override
    public void onInitialize() {
        NeoHammers.init();
        this.addCreativeModeTabContent();
        PlayerBlockBreakEvents.BEFORE.register(this::onBreakBlock);
        LootTableEvents.MODIFY.register(this::modifyLootTables);
        ConfigRegistry.INSTANCE.register(NeoHammers.MOD_ID, ModConfig.Type.COMMON, NeoHammers.CONFIG_SPEC, NeoHammers.MOD_ID + "-local.toml");
    }

    private boolean onBreakBlock(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        if (player instanceof ServerPlayer serverPlayer) {
            ModEvents.onPlayerBreakBlock(serverPlayer, pos, state, level);
        }

        return true;
    }

    private void modifyLootTables(ResourceKey<LootTable> key, LootTable.Builder builder, LootTableSource source, HolderLookup.Provider provider) {
        var enchantmentLookup = provider.lookupOrThrow(Registries.ENCHANTMENT);
        var enchantment = enchantmentLookup.getOrThrow(ModEnchantments.HAMMERING);
        if (source.isBuiltin() && key.equals(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS)) {
            LootPool.Builder pool = LootPool.lootPool()
                    .add(LootItem.lootTableItem(Items.BOOK))
                    .apply(new SetEnchantmentsFunction.Builder().withEnchantment(enchantment, ContextIntProviders.between(4, 6)).build())
                    .when(LootItemRandomChanceCondition.randomChance(0.2f));

            builder.withPool(pool);
        }
    }

    private void addCreativeModeTabContent() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register((event) -> {
            event.insertAfter(Items.WOODEN_HOE, new ItemStack(ModItems.WOODEN_HAMMER.get()));
            event.insertAfter(Items.STONE_HOE, new ItemStack(ModItems.STONE_HAMMER.get()));
            event.insertAfter(Items.COPPER_HOE, new ItemStack(ModItems.COPPER_HAMMER.get()));
            event.insertAfter(Items.IRON_HOE, new ItemStack(ModItems.IRON_HAMMER.get()));
            event.insertAfter(Items.GOLDEN_HOE, new ItemStack(ModItems.GOLDEN_HAMMER.get()));
            event.insertAfter(Items.DIAMOND_HOE, new ItemStack(ModItems.DIAMOND_HAMMER.get()));
            event.insertAfter(Items.NETHERITE_HOE, new ItemStack(ModItems.NETHERITE_HAMMER.get()));
        });
    }
}
