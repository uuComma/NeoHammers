package com.github.uucomma.neohammers.fabric;

import com.github.uucomma.neohammers.NeoHammers;
import com.github.uucomma.neohammers.common.event.ModEvents;
import com.github.uucomma.neohammers.common.item.ModItems;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.config.ModConfig;
import org.jspecify.annotations.Nullable;

public final class NeoHammersFabric implements ModInitializer  {
    @Override
    public void onInitialize() {
        NeoHammers.init();
        this.addCreativeModeTabContent();
        PlayerBlockBreakEvents.AFTER.register(this::onBreakBlock);
        ConfigRegistry.INSTANCE.register(NeoHammers.MOD_ID, ModConfig.Type.COMMON, NeoHammers.CONFIG_SPEC, NeoHammers.MOD_ID + "-local.toml");
    }

    private void onBreakBlock(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        if (player instanceof ServerPlayer serverPlayer) {
            ModEvents.onPlayerBreakBlock(serverPlayer, pos, state, level);
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
