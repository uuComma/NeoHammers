package com.github.uucomma.neohammers.xplat.service;

import com.github.uucomma.neohammers.xplat.EnvironmentType;
import com.github.uucomma.neohammers.xplat.ModLoader;

public interface PlatformService {
    ModLoader getLoader();
    EnvironmentType getEnvironment();
}
