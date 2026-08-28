package com.github.uucomma.neohammers;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ModCommonConfig {
    public final ModConfigSpec.BooleanValue disableHammeringEnchantingTableRestriction;

    protected ModCommonConfig(ModConfigSpec.Builder builder) {
        this.disableHammeringEnchantingTableRestriction = builder
                .comment("If enabled, allows you to get Hammering with levels above 3 via the enchanting table")
                .translation("config." + NeoHammers.MOD_ID + ".disableHammeringEnchantingTableRestriction")
                .define("disableHammeringEnchantingTableRestriction", false);
    }
}
