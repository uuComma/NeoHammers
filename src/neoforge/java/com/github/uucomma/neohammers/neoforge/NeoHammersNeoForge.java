package com.github.uucomma.neohammers.neoforge;

import com.github.uucomma.neohammers.NeoHammers;
import com.github.uucomma.neohammers.common.event.ModEvents;
import com.github.uucomma.neohammers.common.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;

@Mod(NeoHammers.MOD_ID)
public final class NeoHammersNeoForge {
    public NeoHammersNeoForge(IEventBus eventBus, ModContainer modContainer) {
        NeoHammers.init();
        eventBus.register(this);
        NeoForge.EVENT_BUS.addListener(this::onBreakBlock);
    }

    @SubscribeEvent
    private void addCreativeModeTabContent(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CreativeModeTabs.TOOLS_AND_UTILITIES)) {
            event.insertAfter(new ItemStack(Items.WOODEN_HOE), new ItemStack(ModItems.WOODEN_HAMMER.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.STONE_HOE), new ItemStack(ModItems.STONE_HAMMER.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.COPPER_HOE), new ItemStack(ModItems.COPPER_HAMMER.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.IRON_HOE), new ItemStack(ModItems.IRON_HAMMER.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.GOLDEN_HOE), new ItemStack(ModItems.GOLDEN_HAMMER.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.DIAMOND_HOE), new ItemStack(ModItems.DIAMOND_HAMMER.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_HOE), new ItemStack(ModItems.NETHERITE_HAMMER.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }

    private void onBreakBlock(BreakBlockEvent event) {
        Player player = event.getPlayer();
        LevelAccessor level = event.getLevel();
        BlockPos hitPos = event.getPos();
        BlockState hitState = event.getState();
        if (player instanceof ServerPlayer serverPlayer) {
            ModEvents.onPlayerBreakBlock(serverPlayer, hitPos, hitState, level);
        }
    }
}
