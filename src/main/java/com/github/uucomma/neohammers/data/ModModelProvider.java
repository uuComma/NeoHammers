package com.github.uucomma.neohammers.data;

import com.github.uucomma.neohammers.NeoHammers;
import com.github.uucomma.neohammers.common.item.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, NeoHammers.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        itemModels.generateFlatItem(ModItems.WOODEN_HAMMER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.STONE_HAMMER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.COPPER_HAMMER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.IRON_HAMMER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.GOLDEN_HAMMER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.DIAMOND_HAMMER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.NETHERITE_HAMMER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
    }
}
