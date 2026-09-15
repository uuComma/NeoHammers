package com.github.uucomma.neohammers.common.advancement;

import com.github.uucomma.neohammers.common.enchantment.ModEnchantmentEffectComponents;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jspecify.annotations.NullMarked;

import java.util.Optional;

@NullMarked
public class UseExtendedAreaMineTrigger extends SimpleCriterionTrigger<UseExtendedAreaMineTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, ItemStack stack) {
        this.trigger(player, (triggerInstance) -> triggerInstance.matches(stack));
    }

    public record TriggerInstance(
            Optional<ContextAwarePredicate> player,
            Optional<Integer> minLevel
    ) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                (instance) -> instance.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                        Codec.INT.optionalFieldOf("minLevel").forGetter(TriggerInstance::minLevel)
                ).apply(instance, TriggerInstance::new)
        );

        public static Criterion<TriggerInstance> instance(ContextAwarePredicate player, int minLevel) {
            return ModCriterionTriggers.USE_EXTENDED_AREA_MINE_TRIGGER.get().createCriterion(new TriggerInstance(Optional.of(player), Optional.of(minLevel)));
        }

        public static Criterion<TriggerInstance> instance(int minLevel) {
            return ModCriterionTriggers.USE_EXTENDED_AREA_MINE_TRIGGER.get().createCriterion(new TriggerInstance(Optional.empty(), Optional.of(minLevel)));
        }

        public boolean matches(ItemStack stack) {
            Pair<?, Integer> highestLevel = EnchantmentHelper.getHighestLevel(stack, ModEnchantmentEffectComponents.EXTENDED_AREA_MINE.get());
            return highestLevel.getSecond() >= this.minLevel.orElse(1);
        }
    }
}
