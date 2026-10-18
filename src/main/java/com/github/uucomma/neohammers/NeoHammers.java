package com.github.uucomma.neohammers;

import com.github.uucomma.neohammers.common.advancement.ModCriterionTriggers;
import com.github.uucomma.neohammers.common.enchantment.ModEnchantmentEffectComponents;
import com.github.uucomma.neohammers.common.item.ModItems;
import com.github.uucomma.neohammers.xplat.service.Services;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NeoHammers {
    public static final String MOD_ID = "neohammers";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // TODO: ominous vault loot, config
    public static void init() {
        LOGGER.info("Loading {} for {}", MOD_ID, Services.PLATFORM.getLoader());
        LOGGER.info("Initializing...");
        ModItems.register();
        ModEnchantmentEffectComponents.register();
        ModCriterionTriggers.register();
        LOGGER.info("Initialized.");
    }

    public static Identifier resource(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static MutableComponent translate(String prefix, String suffix) {
        return Component.translatable(prefix + MOD_ID + suffix);
    }
}
