package com.github.uucomma.neohammers.neoforge.plat.service;

import com.github.uucomma.neohammers.xplat.EnvironmentType;
import com.github.uucomma.neohammers.xplat.ModLoader;
import com.github.uucomma.neohammers.xplat.service.PlatformService;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;

public class PlatformServiceImpl implements PlatformService {
    @Override
    public ModLoader getLoader() {
        return ModLoader.NEOFORGE;
    }

    @Override
    public EnvironmentType getEnvironment() {
        return FMLEnvironment.getDist() == Dist.CLIENT ? EnvironmentType.CLIENT : EnvironmentType.SERVER;
    }
}
