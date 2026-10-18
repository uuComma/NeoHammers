package com.github.uucomma.neohammers.neoforge.mixin.client;

import com.github.uucomma.neohammers.client.ModClientEvents;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Inject(method = "submitFeatures", at = @At("TAIL"))
    public void neohammers$afterSubmitFeatures(LevelRenderState levelRenderState, SubmitNodeCollector submitNodeCollector, boolean renderOutline, CallbackInfo ci, @Local(name = "poseStack") PoseStack poseStack) {
        ModClientEvents.renderExtraOutlines((LevelRenderer) (Object) this, levelRenderState, poseStack);
    }
}
