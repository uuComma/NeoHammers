package com.github.uucomma.neohammers.data;

import com.github.uucomma.neohammers.NeoHammers;
import com.github.uucomma.neohammers.common.enchantment.ModEnchantments;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.tags.EnchantmentTags;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public class ModEnchantmentTagsProvider extends EnchantmentTagsProvider {
    public ModEnchantmentTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, NeoHammers.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(EnchantmentTags.IN_ENCHANTING_TABLE)
                .add(ModEnchantments.HAMMERING);
    }
}
