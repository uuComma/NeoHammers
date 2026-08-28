package com.github.uucomma.neohammers.common.event;

import com.github.uucomma.neohammers.NeoHammers;
import com.github.uucomma.neohammers.common.advancement.ModCriterionTriggers;
import com.github.uucomma.neohammers.common.enchantment.ModEnchantmentEffectComponents;
import com.github.uucomma.neohammers.common.item.HammerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;

import java.util.stream.Stream;

@EventBusSubscriber(modid = NeoHammers.MOD_ID)
public final class ModEvents {
    @SubscribeEvent
    public static void onPlayerBreakBlock(BreakBlockEvent event) {
        Player player = event.getPlayer();
        BlockPos hitPos = event.getPos();
        BlockState hitState = event.getState();
        LevelAccessor level = event.getLevel();
        ItemStack stack = player.getMainHandItem();
        if (level.isClientSide()) {
            return;
        }

        if (EnchantmentHelper.has(stack, ModEnchantmentEffectComponents.EXTENDED_AREA_MINE.get()) && HammerItem.canPlayerUseExtendedAreaMine(player)) {
            Stream<BlockPos> blocks = HammerItem.getBlocksInRadiusBasedOnEnchantment(player, hitPos, level);
            blocks.forEach((pos) -> {
                if (pos.equals(hitPos)) {
                    return;
                }

                BlockState state = level.getBlockState(pos);
                if (!HammerItem.canMineOther(stack, hitState, state)) {
                    return;
                }

                state.getBlock().playerDestroy(player.level(), player, pos, state, level.getBlockEntity(pos), stack);
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            });

            ModCriterionTriggers.USE_EXTENDED_AREA_MINE_TRIGGER.get().trigger((ServerPlayer) player, stack);
        }
    }
}