package com.github.uucomma.neohammers.mixin;

import com.github.uucomma.neohammers.NeoHammers;
import com.github.uucomma.neohammers.common.enchantment.ModEnchantments;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Stream;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @Inject(method = "getAvailableEnchantmentResults", at = @At("RETURN"))
    private static void neohammers$restrictHammeringLevel(int level, ItemStack stack, Stream<Holder<Enchantment>> possibleEnchantments, CallbackInfoReturnable<List<EnchantmentInstance>> cir) {
        if (NeoHammers.CONFIG.disableHammeringEnchantingTableRestriction.get()) {
            return;
        }

        List<EnchantmentInstance> list = cir.getReturnValue();
        for (int i = 0; i < list.size(); i++) {
            EnchantmentInstance enchantment = list.get(i);
            if (enchantment.enchantment().is(ModEnchantments.HAMMERING) && enchantment.level() > 3) {
                list.set(i, new EnchantmentInstance(enchantment.enchantment(), 3));
            }
        }
    }
}
