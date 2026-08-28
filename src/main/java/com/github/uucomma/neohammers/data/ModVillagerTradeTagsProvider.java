package com.github.uucomma.neohammers.data;

import com.github.uucomma.neohammers.NeoHammers;
import com.github.uucomma.neohammers.common.ModVillagerTrades;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.VillagerTradeTags;
import net.minecraft.world.item.trading.VillagerTrade;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public class ModVillagerTradeTagsProvider extends TagsProvider<VillagerTrade> {
    protected ModVillagerTradeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.VILLAGER_TRADE, lookupProvider, NeoHammers.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        getOrCreateRawBuilder(VillagerTradeTags.LIBRARIAN_LEVEL_2)
                .addElement(ModVillagerTrades.LIBRARIAN_HAMMERING.identifier());
    }
}
