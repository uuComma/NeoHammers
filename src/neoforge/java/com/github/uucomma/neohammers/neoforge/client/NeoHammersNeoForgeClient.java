package com.github.uucomma.neohammers.neoforge.client;

import com.github.uucomma.neohammers.NeoHammers;
import com.github.uucomma.neohammers.client.ModClientEvents;
import com.github.uucomma.neohammers.xplat.EnvironmentOnly;
import com.github.uucomma.neohammers.xplat.EnvironmentType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ExtractLevelRenderStateEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
//import net.neoforged.neoforge.client.gui.ConfigurationScreen;
//import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = NeoHammers.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = NeoHammers.MOD_ID, value = Dist.CLIENT)
@EnvironmentOnly(EnvironmentType.CLIENT)
public final class NeoHammersNeoForgeClient {
    public NeoHammersNeoForgeClient(ModContainer modContainer) {
        //modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    public static void renderExtraRenderStates(RenderLevelStageEvent.AfterSky event) {
        ModClientEvents.renderExtraOutlines(event.getLevelRenderer(), event.getLevelRenderState(), event.getPoseStack());
    }

    @SubscribeEvent
    public static void extractExtraRenderStates(ExtractLevelRenderStateEvent event) {
        ModClientEvents.extractExtraRenderState(event.getRenderState(), event.getLevel());
    }
}
