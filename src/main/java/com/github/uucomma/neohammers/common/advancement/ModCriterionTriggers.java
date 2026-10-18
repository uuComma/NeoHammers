package com.github.uucomma.neohammers.common.advancement;

import com.github.uucomma.neohammers.NeoHammers;
import com.github.uucomma.neohammers.xplat.registry.RegistryHelper;
import com.github.uucomma.neohammers.xplat.registry.RegistrySupplier;
import com.github.uucomma.neohammers.xplat.service.Services;
import net.minecraft.advancements.triggers.CriterionTrigger;
import net.minecraft.core.registries.Registries;

public final class ModCriterionTriggers {
    private static final RegistryHelper<CriterionTrigger<?>> HELPER = Services.REGISTRY.create(NeoHammers.MOD_ID, Registries.TRIGGER_TYPE);

    public static final RegistrySupplier<UseExtendedAreaMineTrigger> USE_EXTENDED_AREA_MINE_TRIGGER = HELPER.register("use_extended_area_mine_trigger",
            UseExtendedAreaMineTrigger::new);

    public static void register() {
        HELPER.register();
        NeoHammers.LOGGER.info("Registered Criterion Triggers.");
    }
}
