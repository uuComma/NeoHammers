package com.github.uucomma.neohammers;

import com.github.uucomma.neohammers.common.advancement.ModCriterionTriggers;
import com.github.uucomma.neohammers.common.enchantment.ModEnchantmentEffectComponents;
import com.github.uucomma.neohammers.common.item.ModItems;
import com.github.uucomma.neohammers.common.loot.ModLootModifiers;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;

@Mod(NeoHammers.MOD_ID)
public final class NeoHammers {
    public static final String MOD_ID = "neohammers";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final ModCommonConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    public NeoHammers(IEventBus eventBus, ModContainer modContainer) {
        LOGGER.info("Initializing...");
        eventBus.register(this);
        ModItems.register(eventBus);
        ModEnchantmentEffectComponents.register(eventBus);
        ModLootModifiers.register(eventBus);
        ModCriterionTriggers.register(eventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, CONFIG_SPEC);
        LOGGER.info("Initialized.");
    }

    public static Identifier resource(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static Component translate(String prefix, String suffix) {
        return Component.translatable(prefix + MOD_ID + suffix);
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

    static {
        var configPair = new ModConfigSpec.Builder().configure(ModCommonConfig::new);
        CONFIG = configPair.getLeft();
        CONFIG_SPEC = configPair.getRight();
    }
}
