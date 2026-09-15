package com.github.uucomma.neohammers.common;

import com.github.uucomma.neohammers.NeoHammers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.trading.VillagerTrade;

public class ModVillagerTrades {
    public static final ResourceKey<VillagerTrade> LIBRARIAN_HAMMERING = trade("librarian/hammering");

    private static ResourceKey<VillagerTrade> trade(String id) {
        return ResourceKey.create(Registries.VILLAGER_TRADE, NeoHammers.resource(id));
    }
}
