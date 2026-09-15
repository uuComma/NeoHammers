package com.github.uucomma.neohammers.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LevelRenderer.class)
public interface LevelRendererAccessor {
    @Invoker("submitHitOutline")
    void renderOutline(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, RenderType renderType, BlockOutlineRenderState state, int color, float width, boolean afterTerrain);

    @Accessor("submitNodeStorage")
    SubmitNodeStorage getSubmitNodeStorage();
}