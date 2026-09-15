package com.github.uucomma.neohammers.data;

import com.github.uucomma.neohammers.NeoHammers;
import com.github.uucomma.neohammers.common.item.ModItems;
import com.github.uucomma.neohammers.common.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, NeoHammers.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.ItemTags.HAMMERS)
                .add(ModItems.WOODEN_HAMMER.getKey())
                .add(ModItems.STONE_HAMMER.getKey())
                .add(ModItems.COPPER_HAMMER.getKey())
                .add(ModItems.IRON_HAMMER.getKey())
                .add(ModItems.GOLDEN_HAMMER.getKey())
                .add(ModItems.DIAMOND_HAMMER.getKey())
                .add(ModItems.NETHERITE_HAMMER.getKey());

        tag(Tags.Items.MINING_TOOL_TOOLS)
                .addTag(ModTags.ItemTags.HAMMERS);

        tag(ItemTags.MINING_ENCHANTABLE)
                .addTag(ModTags.ItemTags.HAMMERS);

        tag(ItemTags.MINING_LOOT_ENCHANTABLE)
                .addTag(ModTags.ItemTags.HAMMERS);

        tag(ItemTags.DURABILITY_ENCHANTABLE)
                .addTag(ModTags.ItemTags.HAMMERS);

        tag(ItemTags.VANISHING_ENCHANTABLE)
                .addTag(ModTags.ItemTags.HAMMERS);

        tag(ItemTags.AXES)
                .addTag(ModTags.ItemTags.HAMMERS);

        tag(ItemTags.SHOVELS)
                .addTag(ModTags.ItemTags.HAMMERS);

        tag(ItemTags.HOES)
                .addTag(ModTags.ItemTags.HAMMERS);

        tag(ItemTags.PICKAXES)
                .addTag(ModTags.ItemTags.HAMMERS);
    }
}
