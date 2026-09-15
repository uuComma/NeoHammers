package com.github.uucomma.neohammers.common.advancement;

import com.github.uucomma.neohammers.NeoHammers;
import net.minecraft.advancements.triggers.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCriterionTriggers {
    private static final DeferredRegister<CriterionTrigger<?>> REGISTER = DeferredRegister.create(Registries.TRIGGER_TYPE, NeoHammers.MOD_ID);

    public static final Supplier<UseExtendedAreaMineTrigger> USE_EXTENDED_AREA_MINE_TRIGGER = register("use_extended_area_mine_trigger",
            UseExtendedAreaMineTrigger::new);

    private static<T extends CriterionTrigger<?>> Supplier<T> register(String id, Supplier<T> sup) {
        return REGISTER.register(id, sup);
    }

    public static void register(IEventBus eventBus) {
        REGISTER.register(eventBus);
        NeoHammers.LOGGER.info("Registered Criterion Triggers.");
    }
}
