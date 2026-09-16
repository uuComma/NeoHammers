package com.github.uucomma.neohammers.common.item;

import com.github.uucomma.neohammers.NeoHammers;
import com.github.uucomma.neohammers.common.util.ModTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModItems {
    private static final DeferredRegister.Items REGISTER = DeferredRegister.createItems(NeoHammers.MOD_ID);

    public static final DeferredItem<HammerItem> WOODEN_HAMMER = createHammer("wooden_hammer", ToolMaterial.WOOD, 5, -3.1f,
            of().cookingFuel(ContextIntProviders.COOKING_TIME_WOOD_ITEMS_LARGE));
    public static final DeferredItem<HammerItem> STONE_HAMMER = createHammer("stone_hammer", ToolMaterial.STONE, 6, -3.1f, of());
    public static final DeferredItem<HammerItem> COPPER_HAMMER = createHammer("copper_hammer", ToolMaterial.COPPER, 7, -3.2f, of());
    public static final DeferredItem<HammerItem> IRON_HAMMER = createHammer("iron_hammer", ToolMaterial.IRON, 5, -3.1f, of());
    public static final DeferredItem<HammerItem> GOLDEN_HAMMER = createHammer("golden_hammer", ToolMaterial.GOLD, 5, -3.1f, of());
    public static final DeferredItem<HammerItem> DIAMOND_HAMMER = createHammer("diamond_hammer", ToolMaterial.DIAMOND, 4, -3.1f, of());
    public static final DeferredItem<HammerItem> NETHERITE_HAMMER = createHammer("netherite_hammer", ToolMaterial.NETHERITE, 4, -3, of().fireResistant());

    private static DeferredItem<HammerItem> createHammer(String id, ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties properties) {
        return REGISTER.registerItem(id,
                (props) -> new HammerItem(material, ModTags.BlockTags.MINEABLE_WITH_HAMMER, attackDamage, attackSpeed, 2, props)
        );
    }

    private static Item.Properties of() {
        return new Item.Properties();
    }

    public static void register(IEventBus eventBus) {
        REGISTER.register(eventBus);
        NeoHammers.LOGGER.info("Registered Items.");
    }
}
