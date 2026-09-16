package com.github.uucomma.neohammers.data;

import com.github.uucomma.neohammers.NeoHammers;
import com.github.uucomma.neohammers.common.enchantment.ModEnchantments;
import com.github.uucomma.neohammers.common.loot.AddEnchantedBookLootModifier;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.predicates.AllOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, NeoHammers.MOD_ID);
    }

    @Override
    protected void start() {
        var hammeringEnchantment = this.registries.lookup(Registries.ENCHANTMENT)
                .flatMap((registryLookup) -> registryLookup.get(ModEnchantments.HAMMERING))
                        .orElseThrow();

        add(
                "add_hammering_to_ominous_vault_loot",
                new AddEnchantedBookLootModifier(
                        optionalHolder(
                                AllOfCondition.allOf(
                                        LootTableIdCondition.builder(Identifier.withDefaultNamespace("chests/trial_chambers/reward_ominous")),
                                        LootItemRandomChanceCondition.randomChance(0.2f)
                                ).build()
                        ),
                        0,
                        hammeringEnchantment,
                        3, 6
                )
        );
    }

    private<T> Optional<Holder<T>> optionalHolder(T value) {
        return Optional.of(Holder.direct(value));
    }
}
