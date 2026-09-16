package com.github.uucomma.neohammers.data;

import com.github.uucomma.neohammers.NeoHammers;
import com.github.uucomma.neohammers.common.item.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.recipes.*;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(BootstrapContext<Recipe<?>> context, BootstrapContext<Advancement> advancementContext) {
        super(context, advancementContext);
    }

    @Override
    protected void buildRecipes() {
        createHammer(ItemTags.PLANKS, ModItems.WOODEN_HAMMER.get());
        createHammer(Tags.Items.COBBLESTONES, ModItems.STONE_HAMMER.get());
        createMetalHammer(Tags.Items.STORAGE_BLOCKS_COPPER, ModItems.COPPER_HAMMER.get(), Items.COPPER_INGOT);
        createMetalHammer(Items.IRON_BLOCK, ModItems.IRON_HAMMER.get(), Items.IRON_INGOT);
        createMetalHammer(Items.GOLD_BLOCK, ModItems.GOLDEN_HAMMER.get(), Items.GOLD_INGOT);
        createHammer(Items.DIAMOND_BLOCK, ModItems.DIAMOND_HAMMER.get());
        modNetheriteSmithing(ModItems.DIAMOND_HAMMER.get(), RecipeCategory.TOOLS, ModItems.NETHERITE_HAMMER.get());
    }

    private void createMetalHammerSmeltDown(ItemLike result, ItemLike smeltedResult) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(result), RecipeCategory.TOOLS, CookingBookCategory.MISC, smeltedResult, 0.8f, 13 * 20).unlockedBy(getHasName(result), has(result))
                .save(output, NeoHammers.resource("smelt_" + getItemName(result) + "_back_to_resource").toString());
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(result), RecipeCategory.TOOLS, CookingBookCategory.MISC, smeltedResult, 0.8f, 13 * 20).unlockedBy(getHasName(result), has(result))
                .save(output, NeoHammers.resource("blast_" + getItemName(result) + "_back_to_resource").toString());
    }

    private void createMetalHammer(ItemLike material, ItemLike result, ItemLike smeltedResult) {
        createHammer(material, result);
        createMetalHammerSmeltDown(result, smeltedResult);
    }

    private void createMetalHammer(TagKey<Item> material, ItemLike result, ItemLike smeltedResult) {
        createHammer(material, result);
        createMetalHammerSmeltDown(result, smeltedResult);
    }

    private void createHammer(ItemLike material, ItemLike result) {
        ShapedRecipeBuilder.shaped(items, RecipeCategory.TOOLS, result)
                .pattern(" x ")
                .pattern(" #x")
                .pattern("#  ")
                .define('x', material)
                .define('#', Tags.Items.RODS_WOODEN)
                .unlockedBy(getHasName(material), has(material))
                .save(output);
    }

    private void createHammer(TagKey<Item> material, ItemLike result) {
        ShapedRecipeBuilder.shaped(items, RecipeCategory.TOOLS, result)
                .pattern(" x ")
                .pattern(" #x")
                .pattern("#  ")
                .define('x', material)
                .define('#', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_materials", has(material))
                .save(output);
    }

    private void modNetheriteSmithing(Item ingredientItem, RecipeCategory category, Item resultItem) {
        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                Ingredient.of(ingredientItem),
                Ingredient.of(Items.NETHERITE_INGOT),
                category,
                resultItem
        ).unlocks("has_netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(output, NeoHammers.resource(getItemName(resultItem) + "_smithing").toString());
    }
}
