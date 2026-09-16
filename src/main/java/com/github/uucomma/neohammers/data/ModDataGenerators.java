package com.github.uucomma.neohammers.data;

import com.github.uucomma.neohammers.NeoHammers;
import com.github.uucomma.neohammers.common.ModVillagerTrades;
import com.github.uucomma.neohammers.common.enchantment.ModEnchantments;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = NeoHammers.MOD_ID)
public final class ModDataGenerators {
    private static final RegistrySetBuilder WORLD_REGISTRY_PROVIDERS = new RegistrySetBuilder()
            .add(Registries.ENCHANTMENT, ModEnchantments::bootstrap)
            .add(Registries.VILLAGER_TRADE, ModVillagerTrades::bootstrap);

    private static final RegistrySetBuilder RELOADABLE_REGISTRY_PROVIDERS = new RegistrySetBuilder()
            .add(Registries.ADVANCEMENT, new ModAdvancementProvider())
            .add(RecipeProvider.asBootstrap(ModRecipeProvider::new));

    @SubscribeEvent
    public static void onGatherClientData(GatherDataEvent.Client event) {
        // Assets
        event.createProvider(ModModelProvider::new);

        // Data
        event.createWorldRegistryObjects(WORLD_REGISTRY_PROVIDERS);
        event.createReloadableRegistryObjects(RELOADABLE_REGISTRY_PROVIDERS);
        event.createProvider(ModEnchantmentTagsProvider::new);
        event.createProvider(ModVillagerTradeTagsProvider::new);
        event.createProvider(ModBlockTagsProvider::new);
        event.createProvider(ModItemTagsProvider::new);
        event.createProvider(ModGlobalLootModifierProvider::new);
    }
}
