package com.github.uucomma.neohammers.common.item;

import com.github.uucomma.neohammers.NeoHammers;
import com.github.uucomma.neohammers.common.util.ModTags;
import com.github.uucomma.neohammers.xplat.registry.RegistryHelper;
import com.github.uucomma.neohammers.xplat.registry.RegistrySupplier;
import com.github.uucomma.neohammers.xplat.service.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public final class ModItems {
    private static final RegistryHelper<Item> HELPER = Services.REGISTRY.create(NeoHammers.MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<Item> WOODEN_HAMMER = createHammer("wooden_hammer", ToolMaterial.WOOD, 5, -3.1f, of());
    public static final RegistrySupplier<Item> STONE_HAMMER = createHammer("stone_hammer", ToolMaterial.STONE, 6, -3.1f, of());
    public static final RegistrySupplier<Item> COPPER_HAMMER = createHammer("copper_hammer", ToolMaterial.COPPER, 7, -3.2f, of());
    public static final RegistrySupplier<Item> IRON_HAMMER = createHammer("iron_hammer", ToolMaterial.IRON, 5, -3.1f, of());
    public static final RegistrySupplier<Item> GOLDEN_HAMMER = createHammer("golden_hammer", ToolMaterial.GOLD, 5, -3.1f, of());
    public static final RegistrySupplier<Item> DIAMOND_HAMMER = createHammer("diamond_hammer", ToolMaterial.DIAMOND, 4, -3.1f, of());
    public static final RegistrySupplier<Item> NETHERITE_HAMMER = createHammer("netherite_hammer", ToolMaterial.NETHERITE, 4, -3, of().fireResistant());

    private static RegistrySupplier<Item> createHammer(String id, ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties properties) {
        return HELPER.register(id,
                () -> new HammerItem(
                        material,
                        ModTags.BlockTags.MINEABLE_WITH_HAMMER,
                        attackDamage,
                        attackSpeed,
                        2,
                        properties.setId(HELPER.key(id))
                ));
    }

    private static Item.Properties of() {
        return new Item.Properties();
    }

    public static void register() {
        HELPER.register();
        NeoHammers.LOGGER.info("Registered Items.");
    }
}
