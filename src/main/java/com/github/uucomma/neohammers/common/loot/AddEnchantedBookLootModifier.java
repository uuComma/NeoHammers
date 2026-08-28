package com.github.uucomma.neohammers.common.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class AddEnchantedBookLootModifier extends LootModifier {
    public static final MapCodec<AddEnchantedBookLootModifier> CODEC = RecordCodecBuilder.mapCodec(
            (instance) -> codecStart(instance).and(instance.group(
                    Enchantment.CODEC.fieldOf("enchantment").forGetter(AddEnchantedBookLootModifier::getEnchantment),
                    Codec.INT.fieldOf("minLevel").forGetter(AddEnchantedBookLootModifier::getMinLevel),
                    Codec.INT.fieldOf("maxLevel").forGetter(AddEnchantedBookLootModifier::getMaxLevel)
            )).apply(instance, AddEnchantedBookLootModifier::new));

    private final Holder<Enchantment> enchantment;
    private final int minLevel;
    private final int maxLevel;

    public AddEnchantedBookLootModifier(LootItemCondition[] conditionsIn, int priority, Holder<Enchantment> enchantment, int minLevel, int maxLevel) {
        super(conditionsIn, priority);
        this.enchantment = enchantment;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> loot, LootContext context) {
        for (LootItemCondition condition: this.conditions) {
            if (!condition.test(context)) return loot;
        }

        int level = context.getRandom().nextIntBetweenInclusive(this.minLevel, this.maxLevel);
        loot.add(EnchantmentHelper.createBook(new EnchantmentInstance(this.enchantment, level)));
        return loot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    private Holder<Enchantment> getEnchantment() {
        return enchantment;
    }

    private int getMinLevel() {
        return minLevel;
    }

    private int getMaxLevel() {
        return maxLevel;
    }
}
