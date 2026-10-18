package com.github.uucomma.neohammers.fabric.plat.service;

import com.github.uucomma.neohammers.xplat.EnvironmentType;
import com.github.uucomma.neohammers.xplat.ModLoader;
import com.github.uucomma.neohammers.xplat.service.PlatformService;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

public class PlatformServiceImpl implements PlatformService {
    @Override
    public ModLoader getLoader() {
        return ModLoader.FABRIC;
    }

    @Override
    public EnvironmentType getEnvironment() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? EnvironmentType.CLIENT : EnvironmentType.SERVER;
    }
}
