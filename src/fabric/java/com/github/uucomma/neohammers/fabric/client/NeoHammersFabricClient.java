package com.github.uucomma.neohammers.fabric.client;

import com.github.uucomma.neohammers.client.ModClientEvents;
import com.github.uucomma.neohammers.xplat.EnvironmentOnly;
import com.github.uucomma.neohammers.xplat.EnvironmentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;

@Environment(EnvType.CLIENT)
@EnvironmentOnly(EnvironmentType.CLIENT)
public final class NeoHammersFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        LevelExtractionEvents.END_EXTRACTION.register(this::extractExtraRenderStates);
        LevelRenderEvents.BEFORE_BLOCK_OUTLINE.register(this::renderExtraRenderStates);
    }

    private void extractExtraRenderStates(LevelExtractionContext context) {
        ModClientEvents.extractExtraRenderState(context.levelState(), context.level());
    }

    private boolean renderExtraRenderStates(LevelRenderContext context, BlockOutlineRenderState outlineRenderState) {
        ModClientEvents.renderExtraOutlines(context.levelRenderer(), context.levelState(), context.poseStack());
        return true;
    }
}
