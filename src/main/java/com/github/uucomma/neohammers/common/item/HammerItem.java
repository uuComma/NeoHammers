package com.github.uucomma.neohammers.common.item;

import com.github.uucomma.neohammers.common.enchantment.ModEnchantmentEffectComponents;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NullMarked;

import java.util.stream.Stream;

@NullMarked
public class HammerItem extends Item {
    private final ToolMaterial material;

    public HammerItem(ToolMaterial material, TagKey<Block> minableBlocks, float attackDamage, float attackSpeed, int durabilityModifier, Properties properties) {
        super(properties.tool(material, minableBlocks, attackDamage, attackSpeed, 0)
                .durability(material.durability() * durabilityModifier));
        this.material = material;
    }

    public static Stream<BlockPos> getBlocksInRadius(Player player, int radius, int depth, BlockPos hitBlockPos, LevelAccessor level) {
        Vec3 eyePosition = player.getEyePosition();
        Vec3 rotation = player.getViewVector(1);

        double reach = player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE);

        Vec3 combined = eyePosition.add(rotation.x * reach, rotation.y * reach, rotation.z * reach);
        BlockHitResult result = level.clip(new ClipContext(player.getEyePosition(), combined, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
        if (result.getType() == HitResult.Type.BLOCK) {
            Direction side = result.getDirection();

            boolean doX = side.getStepX() == 0;
            boolean doY = side.getStepY() == 0;
            boolean doZ = side.getStepZ() == 0;

            BlockPos begin = new BlockPos(doX ? -radius : 0, doY ? -radius : 0, doZ ? -radius : 0);
            BlockPos end = new BlockPos(doX ? radius : depth * -side.getStepX(), doY ? radius : depth * -side.getStepY(), doZ ? radius : depth * -side.getStepZ());
            return BlockPos.betweenClosedStream(hitBlockPos.offset(begin), hitBlockPos.offset(end));
        }

        return Stream.of();
    }

    public static Stream<BlockPos> getBlocksInRadiusBasedOnEnchantment(Player player, BlockPos hitBlockPos, LevelAccessor level) {
        ItemStack stack = player.getMainHandItem();
        Pair<?, Integer> highestLevel = EnchantmentHelper.getHighestLevel(stack, ModEnchantmentEffectComponents.EXTENDED_AREA_MINE.get());
        int radius = highestLevel.getSecond() / 4 + 1;
        int depth = highestLevel.getSecond() % 3 - 1;
        if (depth < 0) depth = 2;
        return getBlocksInRadius(player, radius, depth, hitBlockPos, level);

    }

    public static boolean canMineOther(ItemStack stack, BlockState state, BlockState otherState) {
        return stack.getItem().isCorrectToolForDrops(stack, otherState) && otherState.getBlock().defaultDestroyTime() <= state.getBlock().defaultDestroyTime();
    }

    public static boolean canPlayerUseExtendedAreaMine(Player player) {
        return !player.getAbilities().instabuild && !player.isShiftKeyDown();
    }

    public ToolMaterial getMaterial() {
        return this.material;
    }
}