package com.github.uucomma.neohammers.client;

import com.github.uucomma.neohammers.NeoHammers;
import com.github.uucomma.neohammers.common.enchantment.ModEnchantmentEffectComponents;
import com.github.uucomma.neohammers.common.item.HammerItem;
import com.github.uucomma.neohammers.mixin.client.LevelRendererAccessor;
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
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ExtractLevelRenderStateEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@EventBusSubscriber(modid = NeoHammers.MOD_ID, value = Dist.CLIENT)
public final class ModClientEvents {
    private static final ContextKey<List<BlockOutlineRenderState>> extraBlockOutlineRenderStates = new ContextKey<>(NeoHammers.resource("extrablockoutlinerenderstates"));

    @SubscribeEvent
    public static void renderExtraOutlines(RenderLevelStageEvent.AfterTranslucentFeatures event) {
        GameRenderer gameRenderer = Minecraft.getInstance().gameRenderer;
        LevelRenderer levelRenderer = event.getLevelRenderer();
        LevelRenderState levelRenderState = event.getLevelRenderState();
        List<BlockOutlineRenderState> outlineRenderStates = levelRenderState.getRenderDataOrDefault(extraBlockOutlineRenderStates, new ArrayList<>());
        Vec3 camPos = levelRenderState.cameraRenderState.pos;
        PoseStack poseStack = event.getPoseStack();
        SubmitNodeStorage submitNodeStorage = ((LevelRendererAccessor) levelRenderer).getSubmitNodeStorage();
        for (var renderState: outlineRenderStates) {
            BlockPos pos = renderState.pos();
            poseStack.pushPose();
            poseStack.translate(pos.getX() - camPos.x(), pos.getY() - camPos.y(), pos.getZ() - camPos.z());

            RenderType renderType;
            if (renderState.highContrast()) {
                renderType = RenderTypes.linesDepthBias();
            } else if (gameRenderer.useImprovedTransparency()) {
                renderType = RenderTypes.linesTranslucentNoDepthWrite();
            } else {
                renderType = RenderTypes.linesTranslucent();
            }

            ((LevelRendererAccessor) levelRenderer).renderOutline(
                    poseStack,
                    submitNodeStorage,
                    renderType,
                    renderState,
                    renderState.highContrast() ? -11010079 : ARGB.black(102),
                    gameRenderer.gameRenderState().windowRenderState.appropriateLineWidth,
                    renderState.isTranslucent()
            );

            poseStack.popPose();
        }
    }

    @SubscribeEvent
    public static void extractExtraRenderState(ExtractLevelRenderStateEvent event) {
        LevelRenderState levelRenderState = event.getRenderState();
        ClientLevel level = event.getLevel();
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

            List<BlockOutlineRenderState> extraOutlineRenderStateList
                    = levelRenderState.getRenderDataOrDefault(extraBlockOutlineRenderStates, new ArrayList<>(16));

            extraOutlineRenderStateList.clear();

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
                boolean isTranslucent = model.hasMaterialFlag(level, pos, state, BakedQuad.FLAG_TRANSLUCENT);
                extraOutlineRenderStateList.add(new BlockOutlineRenderState(
                        posClone,
                        isTranslucent,
                        highContrast,
                        state.getShape(level, pos),
                        List.of()
                ));
            });

            levelRenderState.setRenderData(extraBlockOutlineRenderStates, extraOutlineRenderStateList);
        }
    }
}
