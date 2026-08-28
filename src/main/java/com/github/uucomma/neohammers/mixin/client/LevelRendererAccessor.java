package com.github.uucomma.neohammers.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LevelRenderer.class)
public interface LevelRendererAccessor {
    @Invoker("renderHitOutline")
    void renderOutline(PoseStack poseStack, VertexConsumer builder, double camX, double camY, double camZ, BlockOutlineRenderState state, int color, float width);

    @Accessor("renderBuffers")
    RenderBuffers getRenderBuffers();
}