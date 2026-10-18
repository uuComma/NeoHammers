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
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
@EnvironmentOnly(EnvironmentType.CLIENT)
public final class NeoHammersFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        LevelExtractionEvents.AFTER_BLOCK_OUTLINE_EXTRACTION.register(this::extractExtraRenderStates);
        LevelRenderEvents.AFTER_TRANSLUCENT_FEATURES.register(this::renderExtraRenderStates);
    }

    private void renderExtraRenderStates(LevelRenderContext context) {
        ModClientEvents.renderExtraOutlines(context.levelRenderer(), context.levelState(), context.poseStack());
    }

    private void extractExtraRenderStates(LevelExtractionContext context, @Nullable HitResult hitResult) {
        ModClientEvents.extractExtraRenderState(context.levelState(), context.level());
    }
}
