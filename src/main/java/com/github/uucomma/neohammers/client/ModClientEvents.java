package com.github.uucomma.neohammers.client;

import com.github.uucomma.neohammers.common.enchantment.ModEnchantmentEffectComponents;
import com.github.uucomma.neohammers.common.item.HammerItem;
import com.github.uucomma.neohammers.mixin.client.LevelRendererAccessor;
import com.github.uucomma.neohammers.xplat.EnvironmentOnly;
import com.github.uucomma.neohammers.xplat.EnvironmentType;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.BlockBreakingRenderState;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

@EnvironmentOnly(EnvironmentType.CLIENT)
public final class ModClientEvents {
    private static final ArrayList<BlockOutlineRenderState> extraBlockOutlineRenderStates = new ArrayList<>(107); // 6x6x3 - 1

    public static void renderExtraOutlines(LevelRenderer levelRenderer, LevelRenderState levelRenderState, PoseStack poseStack) {
        GameRenderer gameRenderer = Minecraft.getInstance().gameRenderer;
        Vec3 camPos = levelRenderState.cameraRenderState.pos;
        LevelRendererAccessor accessor = (LevelRendererAccessor) levelRenderer;
        SubmitNodeStorage submitNodeStorage = accessor.neohammers$getSubmitNodeStorage();
        for (var renderState: extraBlockOutlineRenderStates) {
            BlockPos pos = renderState.pos();
            poseStack.pushPose();
            poseStack.translate(pos.getX() - camPos.x(), pos.getY() - camPos.y(), pos.getZ() - camPos.z());

            RenderType renderType;
            if (renderState.highContrast()) {
                renderType = RenderTypes.linesDepthBias();

                // This renders the black outline of the outline
                accessor.neohammers$submitHitOutline(
                        poseStack,
                        submitNodeStorage,
                        RenderTypes.secondaryBlockOutline(),
                        renderState,
                        0xFF000000,
                        7,
                        renderState.isTranslucent()
                );
            } else if (gameRenderer.useImprovedTransparency()) {
                renderType = RenderTypes.linesTranslucentNoDepthWrite();
            } else {
                renderType = RenderTypes.linesTranslucent();
            }

            accessor.neohammers$submitHitOutline(
                    poseStack,
                    submitNodeStorage,
                    renderType,
                    renderState,
                    renderState.highContrast() ? 0xFF57FFE1 : ARGB.black(102),
                    gameRenderer.gameRenderState().windowRenderState.appropriateLineWidth,
                    renderState.isTranslucent()
            );

            poseStack.popPose();
        }
    }

    public static void extractExtraRenderState(LevelRenderState levelRenderState, ClientLevel level) {
        extraBlockOutlineRenderStates.clear();
        Player player = Minecraft.getInstance().player;
        if (player != null && HammerItem.canPlayerUseExtendedAreaMine(player) && Minecraft.getInstance().hitResult instanceof BlockHitResult hitResult) {
            ItemStack stack = player.getMainHandItem();
            BlockPos hitPos = hitResult.getBlockPos();
            BlockState hitState = level.getBlockState(hitPos);
            if (!EnchantmentHelper.has(stack, ModEnchantmentEffectComponents.EXTENDED_AREA_MINE.get())) {
                return;
            }

            int breakProgress = levelRenderState.blockBreakingRenderStates.stream()
                    .filter(state -> state.blockPos().equals(hitPos))
                    .findFirst().map(BlockBreakingRenderState::progress)
                    .orElse(-1);

            boolean highContrast = Minecraft.getInstance().options.highContrastBlockOutline().get();
            HammerItem.getBlocksInRadiusBasedOnEnchantment(player, hitPos, level).forEach((pos) -> {
                if (pos.equals(hitPos)) {
                    return;
                }

                BlockState state = level.getBlockState(pos);
                if (!HammerItem.canMineOther(stack, hitState, state)) {
                    return;
                }

                // This fixes a bug where the block positions change before rendering
                BlockPos posClone = new BlockPos(pos.getX(), pos.getY(), pos.getZ());

                if (breakProgress != -1) {
                    levelRenderState.blockBreakingRenderStates.add(new BlockBreakingRenderState(posClone, state, breakProgress));
                }

                BlockStateModel model = Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(state);
                boolean isTranslucent = model.hasMaterialFlag(BakedQuad.FLAG_TRANSLUCENT);
                extraBlockOutlineRenderStates.add(new BlockOutlineRenderState(
                        posClone,
                        isTranslucent,
                        highContrast,
                        state.getShape(level, pos)
                ));
            });
        }
    }
}

